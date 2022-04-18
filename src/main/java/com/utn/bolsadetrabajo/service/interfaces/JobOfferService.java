package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.request.PostulateDTO;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

public interface JobOfferService extends Removable, Writeable<JobOfferDTO> {

    ResponseEntity<?> getJobOfferById(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> postulate(PostulateDTO postulateDTO);

    ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO);
}
