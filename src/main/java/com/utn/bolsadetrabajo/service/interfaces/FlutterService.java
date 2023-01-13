package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationFlutterDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferFlutterDTO;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface FlutterService {

    ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> logoutUserFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id);

    ResponseEntity<?> getJobOfferAllByPublisher(Long id);

    ResponseEntity<?> getAllAppliedByJobOffer(Long id);

    ResponseEntity<?> getJobOfferEvaluation(JobOfferEvaluationFlutterDTO dto);

    ResponseEntity<?> create(PersonDTO personDTO);

    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> updateJobOffer(JobOfferFlutterDTO jobOfferFlutterDTO);
}
