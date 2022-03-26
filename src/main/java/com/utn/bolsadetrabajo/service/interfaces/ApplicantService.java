package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApplicantService {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> update(Long id, PersonDTO personDTO);

    ResponseEntity<?> save(PersonDTO personDTO) throws PersonException;

    ResponseEntity<?> getByIdUserApp(User user);

    void postulateJobOffer(Applicant applicant);

    List<Applicant> getAll();

    Applicant getApplicantByUser(User user);
}
