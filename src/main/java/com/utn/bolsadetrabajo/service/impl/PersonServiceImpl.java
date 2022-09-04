package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.interfaces.*;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.reports.GenerateListTypePerson;
import com.utn.bolsadetrabajo.util.Errors;
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
public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired private PersonRepository repository;
    @Autowired private EmailGoogleService emailGoogleService;
    @Autowired private PersonMapper mapper;
    @Autowired private MessageSource messageSource;
    @Autowired private UserService userService;
    @Autowired private Validator validator;
    @Autowired private GenerateListTypePerson generateListTypePerson;
    @Autowired private PublisherService publisherService;
    @Autowired private ApplicantService applicantService;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePerson(person, messageSource.getMessage("person.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.search.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("person.search.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException {
        return id > 0L ? updatePerson(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            repository.save(mapper.deletePerson(getPerson(id)));
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("person.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("person.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        return allPersonList(mapper.toPersonList(repository.findAll()));
    }

    @Override
    public ResponseEntity<?> getAllApplicant(int numberPage) {
        return allPersonList(mapper.toApplicantList(applicantService.getAll()));
    }

    @Override
    public ResponseEntity<?> getAllPublisher(int numberPage) {
        return allPersonList(mapper.toPublisherList(publisherService.getAll()));
    }

    @Override
    public Person getPersonByUsername(String username) {
        return repository.findByUser(userService.findByUsername(username));
    }

    private ResponseEntity<?> updatePerson(Long id, PersonDTO personDTO) {
        try{
            Person newPer = mapper.toUpdate(getPerson(id), personDTO);
            validator.validPerson(newPer);
            Person aux = repository.save(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePerson(aux, messageSource.getMessage("person.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("person.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("person.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO personDTO) throws PersonException {
        try{
            Person newPerson = mapper.toModel(personDTO);
            validator.validPerson(newPerson);
            Person person = repository.save(newPerson);
            emailGoogleService.createEmailPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePerson(person, messageSource.getMessage("person.created.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("person.create.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("person.create.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.create.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> allPersonList(List<ResponsePersonDto> lists) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

    private Person getPerson(Long id) {
        return repository.findById(id).get();
    }

}
