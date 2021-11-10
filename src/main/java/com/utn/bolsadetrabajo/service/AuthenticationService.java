package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse createJwt(AuthenticationRequest authenticationRequest) throws Exception;
}
