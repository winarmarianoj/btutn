package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.mapper.PublisherMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.PublisherRepository;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherServiceImpl.class);

    @Autowired PublisherRepository repository;
    @Autowired ParametersRepository parametersRepository;
    @Autowired EmailGoogleService emailGoogleService;
    @Autowired PublisherMapper publisherMapper;
    @Autowired PersonMapper personMapper;
    @Autowired MessageSource messageSource;
    @Autowired Validator validator;

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException {
        return id > 0L ? updatePublisher(id, personDTO) : create(personDTO);
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
            LOGGER.error(messageSource.getMessage("publisher.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            Publisher publisher = getPublisher(person.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(publisherMapper.toResponsePublisher(publisher, messageSource.getMessage("publisher.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("publisher.search.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserPub(User user) {
        try{
            Publisher publisher = repository.findByUser(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(publisherMapper.toResponsePublisher(publisher, messageSource.getMessage("publisher.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("publisher.search.failed " + e.getMessage(), new Object[] {user.getUserId()}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed", new Object[] {user.getUserId()}, null));
        }
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

    private ResponseEntity<?> updatePublisher(Long id, PersonDTO publisherDTO) {
        try {
            Publisher newPublisher = publisherMapper.toUpdate(getPublisher(id), publisherDTO);
            validator.validPerson(newPublisher);
            Publisher aux = repository.save(newPublisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toResponsePerson(aux, messageSource.getMessage("publisher.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("publisher.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO personDTO) throws PersonException {
        try{
            Publisher newPublisher = publisherMapper.toModel(null, personDTO);
            validator.validPerson(newPublisher);
            Publisher publisher = repository.save(newPublisher);
            emailGoogleService.createEmailPerson(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toResponsePerson(publisher, messageSource.getMessage("publisher.created.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("publisher.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private Publisher getPublisher(Long id) {
        return repository.findById(id).get();
    }

}
