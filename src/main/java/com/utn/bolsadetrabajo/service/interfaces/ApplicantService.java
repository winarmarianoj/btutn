package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApplicantService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> getByIdUserApp(User user);

    void postulateJobOffer(Applicant applicant);

    List<Applicant> getAll();

    Applicant getApplicantByUser(User user);
}
