package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.service.common.GenericService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApplicantService extends GenericService<PersonDTO> {

    ResponseEntity<?> getByIdUserApp(User user);

    void postulateJobOffer(Applicant applicant);

    ResponseEntity<?> sendGetApplicantByRequest(Applicant applicant, Long id);

    List<Applicant> getAll();

}
