package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.mapper.PublisherMapper;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.PublisherRepository;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired PublisherRepository repository;
    @Autowired ParametersRepository parametersRepository;
    @Autowired EmailGoogleService emailGoogleService;
    @Autowired PublisherMapper publisherMapper;
    @Autowired PersonMapper personMapper;
    @Autowired MessageSource messageSource;
    @Autowired Validator validator;

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            Publisher publisher = repository.findById(person.getId()).get();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(publisherMapper.toResponsePublisher(publisher, messageSource.getMessage("publisher.response.object.success", null,null)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException {
        if(personDTO.getId() != null && personDTO.getId() > ZERO){
            return updatePublisher(personDTO);
        }else {
            return save(personDTO);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Publisher publisher = getPublisher(id);
            publisher.setDeleted(true);
            publisher.getUser().setState(State.DELETED);
            repository.save(publisher);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("publisher.deleted.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserPub(User user) {
        Publisher publisher = repository.findByUser(user);
        return sendGetPersonByRequest(publisher, publisher.getId());
    }

    @Override
    public void addJobOffer(Publisher publisher) {
        repository.save(publisher);
    }

    @Override
    public List<Publisher> getAll() {
        return repository.findAll();
    }

    @Override
    public Publisher getPublisherByUser(User user) {
        return repository.findByUser(user);
    }

    private ResponseEntity<?> updatePublisher(PersonDTO publisherDTO) {
        try {
            Publisher newPublisher = publisherMapper.toUpdate(getPublisher(publisherDTO.getId()), publisherDTO);
            validator.validPerson(newPublisher);
            Publisher aux = repository.save(newPublisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toResponsePerson(aux, messageSource.getMessage("publisher.update.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> save(PersonDTO personDTO) throws PersonException {
        try{
            Publisher newPublisher = publisherMapper.toModel(null, personDTO);
            validator.validPerson(newPublisher);
            Publisher publisher = repository.save(newPublisher);
            emailGoogleService.createEmailPerson(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toResponsePerson(publisher, messageSource.getMessage("publisher.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private Publisher getPublisher(Long id) {
        return repository.findById(id).get();
    }

}
