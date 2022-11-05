package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface FlutterService {

    ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id);
}
