package com.utn.bolsadetrabajo.service.manager;

import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.interfaces.ApplicantService;
import com.utn.bolsadetrabajo.service.interfaces.PersonService;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired PersonRepository repository;
    @Autowired PersonService personService;
    @Autowired ApplicantService applicantService;
    @Autowired PublisherService publisherService;
    @Autowired UserService userService;

    @Override
    public ResponseEntity<?> getById(Long id) {
        Person person = repository.findById(id).get();
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.sendGetPersonByRequest(person, id);
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequest(person, id);
        } else {
            return personService.sendGetPersonByRequest(person, id);
        }
    }

    @Override
    public ResponseEntity<?> getByIdentification(String identification) {
        Person person = repository.findByidentification(identification);
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.sendGetPersonByRequest(person, Long.valueOf(identification));
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequest(person, Long.valueOf(identification));
        } else {
            return personService.sendGetPersonByRequest(person, Long.valueOf(identification));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUser(Long id) {
        User user = getUserById(id);
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
    public Applicant getPersonTypeApplicantByIdUser(Long id) {
        User user = getUserById(id);
        return applicantService.getApplicantByUser(user);
    }

    @Override
    public Publisher getPersonTypePublisherByIdUser(Long id) {
        User user = getUserById(id);
        return publisherService.getPublisherByUser(user);
    }

    private User getUserById(Long id) {
        return userService.findByIdUser(id);
    }

}
