package com.utn.bolsadetrabajo.service.common;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.ApplicantService;
import com.utn.bolsadetrabajo.service.PersonService;
import com.utn.bolsadetrabajo.service.PublisherService;
import com.utn.bolsadetrabajo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GenericServiceImpl implements GenericService<PersonDTO>{

    @Autowired PersonRepository repository;
    @Autowired PersonService personService;
    @Autowired ApplicantService applicantService;
    @Autowired PublisherService publisherService;
    @Autowired UserService userService;

    @Override
    public ResponseEntity<?> getById(Long id) {
        Person person = repository.findById(id).get();
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.getById(id);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.getById(id);
        } else {
            return personService.sendGetPersonByRequest(person, id);
        }
    }

    @Override
    public ResponseEntity<?> getByIdentification(String identification) {
        Person person = repository.findByidentification(identification);
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.getByIdentification(identification);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.getByIdentification(identification);
        } else {
            return personService.sendGetPersonByRequest(person, Long.valueOf(identification));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUser(Long id) {
        User user = userService.findByIdUser(id);
        Person person = repository.findByUser(user);
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.getByIdUserApp(user);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.getByIdUserPub(user);
        } else {
            return personService.sendGetPersonByRequest(person, id);
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) {
        Person person = repository.findById(id).get();
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.update(id, personDTO);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.update(id, personDTO);
        } else {
            return personService.update(person, personDTO);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Person person = repository.findById(id).get();
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.delete(id);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.delete(id);
        } else {
            return personService.delete(person);
        }
    }

    @Override
    public ResponseEntity<?> save(PersonDTO personDTO) throws PersonException {
        if (personDTO.getRole().equals(Roles.APPLICANT)) {
            return applicantService.save(personDTO);
        } else if (personDTO.getRole().equals(Roles.PUBLISHER)) {
            return publisherService.save(personDTO);
        } else {
            return personService.save(personDTO);
        }
    }
}
