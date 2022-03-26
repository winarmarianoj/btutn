package com.utn.bolsadetrabajo.service.reports;

import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.enums.State;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReportLists {

    Pageable generatePageable(int number);

    ResponseEntity<?> getAllWithPage(int numberPage);

    ResponseEntity<?> getJobOfferAllWithFilter(State state);

    ResponseEntity<?> getJobApplicantAllByApplicant(Long id);

    ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(Long id);

    ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO);

    ResponseEntity<?> getJobOfferAllByPublisher(Long id);

    ResponseEntity<?> getJobOfferAllSimplePublisher(Category filter, Long id);

}
