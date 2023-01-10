package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationFlutterDTO;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.UserByFlutterDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface FlutterService {

    ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> logoutUserFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id);

    ResponseEntity<?> getJobOfferAllByPublisher(Long id);

    ResponseEntity<?> getAllAppliedByJobOffer(Long id);

    ResponseEntity<?> getJobOfferEvaluation(JobOfferEvaluationFlutterDTO dto);

    ResponseEntity<?> update(Long id, UserByFlutterDTO UserByFlutterDTO) throws PersonException;

    ResponseEntity<?> create(PersonDTO personDTO);

    ResponseEntity<?> getById(Long id);
}
