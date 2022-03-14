package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.PersonController;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.*;
import com.utn.bolsadetrabajo.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired PersonRepository repository;
    @Autowired ParametersRepository parametersRepository;
    @Autowired EmailService emailService;
    @Autowired PersonMapper mapper;
    @Autowired MessageSource messageSource;
    @Autowired UserService userService;
    @Autowired Validator validator;
    @Autowired PublisherService publisherService;
    @Autowired ApplicantService applicantService;

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePerson(person, messageSource.getMessage("person.response.object.success", null,null)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Person person, PersonDTO personDTO) {
        try{
            Person newPer = mapper.toUpdate(person, personDTO);
            validator.validPerson(newPer);
            Person aux = repository.save(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePerson(aux, messageSource.getMessage("person.update.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Person person) {
        try{
            person.setDeleted(true);
            person.getUser().setState(State.DELETED);
            repository.save(person);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("person.deleted.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.deleted.failed", new Object[] {person.getId()}, null));
        }
    }

    @Override
    public ResponseEntity<?> save(PersonDTO personDTO) throws PersonException {
        try{
            Person newPerson = mapper.toModel(personDTO);
            validator.validPerson(newPerson);
            Person person = repository.save(newPerson);
            emailService.createEmailPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePerson(person, messageSource.getMessage("person.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.create.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll(int numberPage) {
        List<Person> personList = repository.findAll();
        List<ResponsePersonDto> lists = mapper.toPersonList(personList);
        return allPersonList(lists, numberPage);
    }

    @Override
    public ResponseEntity<?> getAllApplicant(int numberPage) {
        List<Applicant> applicants = applicantService.getAll();
        List<ResponsePersonDto> lists = mapper.toApplicantList(applicants);
        return allPersonList(lists, numberPage);
    }

    @Override
    public ResponseEntity<?> getAllPublisher(int numberPage) {
        List<Publisher> publishers = publisherService.getAll();
        List<ResponsePersonDto> lists = mapper.toPublisherList(publishers);
        return allPersonList(lists, numberPage);
    }

    @Override
    public Person getPersonByUsername(String username) {
        User user = userService.findByUsername(username);
        return repository.findByUser(user);
    }

    private ResponseEntity<?> allPersonList(List<ResponsePersonDto> lists, int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage(SIZE_PAGE));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);
        Page<ResponsePersonDto> page = new PageImpl<>(lists, pageable, lists.size());
        try{
            if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
            links.add(linkTo(methodOn(PersonController.class).getAll(numberPage)).withSelfRel());

            if(page.hasPrevious()){
                links.add(linkTo(methodOn(PersonController.class).getAll(numberPage - 1)).withRel(PREV));
            }
            if(page.hasNext()){
                links.add(linkTo(methodOn(PersonController.class).getAll(numberPage + 1)).withRel(NEXT));
            }
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

}
