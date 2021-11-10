package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.EmailService;
import com.utn.bolsadetrabajo.service.PersonService;
import com.utn.bolsadetrabajo.service.UserService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository repository;
    private EmailService emailService;
    private PersonMapper mapper;
    private MessageSource messageSource;
    private UserService userService;
    private Validator validator;

    @Autowired
    public PersonServiceImpl(PersonRepository repository, EmailService emailService, PersonMapper mapper, MessageSource messageSource, UserService userService, Validator validator) {
        this.repository = repository;
        this.emailService = emailService;
        this.mapper = mapper;
        this.messageSource = messageSource;
        this.userService = userService;
        this.validator = validator;
    }

    @Override
    public ResponseEntity<?> getById(Long id) {
        Person person = repository.findById(id).get();
        return getPerson(person, String.valueOf(id));
    }

    private ResponseEntity<?> getPerson(Person person, String request) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePerson(person,
                            messageSource.getMessage("person.response.object.success", null,null))
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("person.search.failed",
                            new Object[] {request}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByDni(String dni) {
        Person person = repository.findByidentification(dni);
        return getPerson(person, dni);
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) {
        try{
            Person person = repository.findById(id).get();
            Person newPer = mapper.toModel(person, personDTO);
            validator.validPerson(newPer);
            Person aux = repository.save(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(
                    mapper.toResponsePerson(aux,
                            messageSource.getMessage("person.update.success", null,null))
            );
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Person person = repository.findById(id).get();
            person.setDeleted(true);
            person.getUser().setState(State.DELETED);
            repository.save(person);
            return ResponseEntity.status(HttpStatus.OK).body(
                    messageSource.getMessage("person.deleted.success", null,null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("person.deleted.failed",
                            new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> save(PersonDTO personDTO) throws PersonException {
        try{
            Person newPerson = mapper.toModel(null, personDTO);
            validator.validPerson(newPerson);
            Person person = repository.save(newPerson);
            emailService.createEmailPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    mapper.toResponsePerson(person,
                            messageSource.getMessage("person.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.create.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public Person getPersonByUsername(String username) {
        User user = userService.findByUsername(username);
        return repository.findByUser(user);
    }

}
