package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.PublisherController;
import com.utn.bolsadetrabajo.dto.request.PublisherDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePublisherList;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.mapper.PublisherMapper;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.PublisherRepository;
import com.utn.bolsadetrabajo.service.*;
import com.utn.bolsadetrabajo.util.UserConnectedService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PublisherServiceImpl implements PublisherService {

    private PublisherRepository repository;
    private ParametersRepository parametersRepository;
    private EmailService emailService;
    private PublisherMapper publisherMapper;
    private PersonMapper personMapper;
    private MessageSource messageSource;
    private UserConnectedService userConnectedService;
    private PersonService personService;
    private Validator validator;

    private List<Link> links;

    @Autowired
    public PublisherServiceImpl(PublisherRepository repository, ParametersRepository parametersRepository, EmailService emailService, PublisherMapper publisherMapper, PersonMapper personMapper, MessageSource messageSource, UserConnectedService userConnectedService, PersonService personService, Validator validator) {
        this.repository = repository;
        this.parametersRepository = parametersRepository;
        this.emailService = emailService;
        this.publisherMapper = publisherMapper;
        this.personMapper = personMapper;
        this.messageSource = messageSource;
        this.userConnectedService = userConnectedService;
        this.personService = personService;
        this.validator = validator;
    }

    @Override
    public ResponseEntity<?> save(PublisherDTO dto) throws PersonException{
        try{
            Publisher newPublisher = publisherMapper.toModel(null, dto);
            validator.validPerson(newPublisher);
            Publisher publisher = repository.save(newPublisher);
            emailService.createEmailPerson(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    personMapper.toResponsePerson(publisher,
                            messageSource.getMessage("publisher.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getPublisherById(Long id) {
        Publisher pub = repository.findById(id).get();
        return sendGetPublisherByRequest(pub, String.valueOf(id));
    }

    @Override
    public ResponseEntity<?> getPublisherByCuit(String cuit) {
        Publisher pub = repository.findByIdentification(cuit);
        return sendGetPublisherByRequest(pub, cuit);
    }

    @Override
    public ResponseEntity<?> update(Long id, PublisherDTO publisherDTO) {
        try {
            Publisher pub = repository.findById(id).get();
            Publisher newPublisher = publisherMapper.toModel(pub, publisherDTO);
            validator.validPerson(newPublisher);
            Publisher aux = repository.save(newPublisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    personMapper.toResponsePerson(aux,
                            messageSource.getMessage("publisher.update.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Publisher pub = repository.findById(id).get();
            pub.setDeleted(true);
            pub.getUser().setState(State.DELETED);
            repository.save(pub);
            return ResponseEntity.status(HttpStatus.OK).body(
                    messageSource.getMessage("publisher.deleted.success", null,null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("publisher.deleted.failed",
                            new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllPublishers(int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);
        List<Publisher> publishers = repository.findAll();
        List<ResponsePublisherList> lists = publisherMapper.toPublisherList(publishers);
        Page<ResponsePublisherList> page = new PageImpl<>(lists, pageable, lists.size());
        links = new ArrayList<>();
        try{
            if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
            links.add(linkTo(methodOn(PublisherController.class).getAll(numberPage)).withSelfRel());

            if(page.hasPrevious()){
                links.add(linkTo(methodOn(PublisherController.class)
                        .getAll(numberPage - 1)).withRel("prev"));
            }
            if(page.hasNext()){
                links.add(linkTo(methodOn(PublisherController.class)
                        .getAll(numberPage + 1)).withRel("next"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("publisher.all.publisher.failed",
                            null, null));
        }
    }

    @Override
    public void addJobOffer(Publisher publisher) {
        repository.save(publisher);
    }

    private ResponseEntity<?> sendGetPublisherByRequest(Publisher publisher, String request) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    personMapper.toResponsePerson(publisher,
                            messageSource.getMessage("publisher.response.object.success", null,null))
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("publisher.search.failed",
                            new Object[] {request}, null));
        }
    }

}
