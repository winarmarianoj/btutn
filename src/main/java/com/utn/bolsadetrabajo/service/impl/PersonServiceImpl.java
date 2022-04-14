package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.interfaces.*;
import com.utn.bolsadetrabajo.service.reports.GenerateListTypePerson;
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

    @Autowired PersonRepository repository;
    @Autowired EmailService emailService;
    @Autowired PersonMapper mapper;
    @Autowired MessageSource messageSource;
    @Autowired UserService userService;
    @Autowired Validator validator;
    @Autowired GenerateListTypePerson generateListTypePerson;
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
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) {
        try{
            Person person = getPerson(id);
            Person newPer = mapper.toUpdate(person, personDTO);
            validator.validPerson(newPer);
            Person aux = repository.save(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePerson(aux, messageSource.getMessage("person.update.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Person person = mapper.deletePerson(getPerson(id));
            repository.save(person);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("person.deleted.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.deleted.failed", new Object[] {id}, null));
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
    public ResponseEntity<?> getAll() {
        List<Person> personList = repository.findAll();
        return allPersonList(mapper.toPersonList(personList));
    }

    @Override
    public ResponseEntity<?> getAllApplicant(int numberPage) {
        List<Applicant> applicants = applicantService.getAll();
        return allPersonList(mapper.toApplicantList(applicants));
    }

    @Override
    public ResponseEntity<?> getAllPublisher(int numberPage) {
        List<Publisher> publishers = publisherService.getAll();
        return allPersonList(mapper.toPublisherList(publishers));
    }

    @Override
    public Person getPersonByUsername(String username) {
        User user = userService.findByUsername(username);
        return repository.findByUser(user);
    }

    private ResponseEntity<?> allPersonList(List<ResponsePersonDto> lists) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

    private Person getPerson(Long id) {
        return repository.findById(id).get();
    }

}
