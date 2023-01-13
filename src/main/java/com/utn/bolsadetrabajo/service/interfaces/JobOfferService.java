package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferFlutterDTO;
import com.utn.bolsadetrabajo.dto.request.PostulateDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

public interface JobOfferService extends Removable, Writeable<JobOfferDTO> {
    ResponseEntity<?> create(Long id, JobOfferDTO entity) throws PersonException;

    ResponseEntity<?> getJobOfferById(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> getAllPublished();

    ResponseEntity<?> getJobOfferPending();

    ResponseEntity<?> postulate(PostulateDTO postulateDTO);

    ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO);

    JobOffer getJobOffer(Long id);

    JobOffer updateJobOffer(JobOffer jobOffer, JobOfferFlutterDTO jobOfferFlutterDTO);

    JobOffer saveJobOffer(JobOffer newJobOffer);

    ResponseEntity<?> toResponsePublisherJobOffer(JobOffer aux, String message);

}
