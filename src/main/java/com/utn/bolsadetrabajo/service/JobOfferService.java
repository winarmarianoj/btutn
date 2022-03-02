package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.enums.State;
import org.springframework.http.ResponseEntity;

public interface JobOfferService {

    ResponseEntity<?> save(JobOfferDTO jobOfferDTO);

    ResponseEntity<?> getJobOfferById(Long id);

    ResponseEntity<?> update(Long id, JobOfferDTO jobOfferDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> getAllWithFilter(int page, State state);

    ResponseEntity<?> postulate(Long id);

    ResponseEntity<?> getJobApplicantAllByApplicant(int page);

    ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(int page, Long id);

    ResponseEntity<?> getEvaluationAllJobOffers(JobOfferEvaluationDTO jobOfferEvaluationDTO);

    ResponseEntity<?> getAllJobOfferSimplePublisher(int page, Category filter);

    ResponseEntity<?> getAll(int page);

    ResponseEntity<?> getAllWithoutPage();
}
