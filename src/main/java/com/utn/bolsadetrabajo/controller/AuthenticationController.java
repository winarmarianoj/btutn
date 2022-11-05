package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Messages;
import com.utn.bolsadetrabajo.dto.request.ForgotDTO;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.service.interfaces.AuthenticationService;
import com.utn.bolsadetrabajo.service.interfaces.FlutterService;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "Authentication Controller", description = "Controlador de Autentificaciones.")
@RequestMapping("/auth")
public class AuthenticationController implements Messages {

    @Autowired private AuthenticationService authenticationService;
    @Autowired private UserService userService;
    @Autowired private FlutterService flutterService;

    @ApiOperation(value = "${authentication.createAuthenticationToken} - Devuelve JWT y Datos del User", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return authenticationService.createJwt(authenticationRequest);
    }

    @ApiOperation(value = "${authentication.createAuthenticationToken} - Devuelve JWT y Datos del User", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/loginflutter", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAuthenticationTokenByFlutter(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return flutterService.createJwtByFlutter(authenticationRequest);
    }

    @ApiOperation(value = "${authentication.createAuthenticationToken} - Devuelve JWT y Datos del User", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/forgot", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> changePassword(@RequestBody @Valid ForgotDTO forgotDTO) throws Exception {
        return userService.forgot(forgotDTO);
    }

    @ApiOperation(value = "${authentication.activateNewUser} - Activa un User nuevo", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/activate/{username}/{hash}")
    public ResponseEntity<?> activateNewUser(@PathVariable String username, @PathVariable String hash){
        return userService.activateAccount(username, hash);
    }
}
