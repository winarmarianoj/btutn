package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import org.springframework.http.ResponseEntity;

public interface JobOfferService {

    ResponseEntity<?> getJobOfferById(Long id);

    ResponseEntity<?> update(Long id, JobOfferDTO jobOfferDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> postulate(Long id);

}
