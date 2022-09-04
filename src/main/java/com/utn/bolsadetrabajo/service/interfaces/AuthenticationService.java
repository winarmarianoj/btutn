package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> createJwt(AuthenticationRequest authenticationRequest) throws Exception;
}
