package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.ApplicantDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Applicant;
import org.springframework.http.ResponseEntity;

public interface ApplicantService {
    ResponseEntity<?> save(ApplicantDTO dto) throws PersonException;

    ResponseEntity<?> getApplicantById(Long id);

    ResponseEntity<?> getApplicantByDni(String dni);

    ResponseEntity<?> update(Long id, ApplicantDTO applicantDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> getAllApplicants(int page);

    void postulateJobOffer(Applicant applicant);
}
