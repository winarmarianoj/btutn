package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.service.interfaces.FlutterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/flutter")
public class FlutterController {

    @Autowired FlutterService flutterService;

    @ApiOperation(value = "flutter.getJobApplicantAllByApplicantByFlutter - Applicant: Ver su lista de postulaciones. El ID que se recibe es el del Applicant quien consulta.", response = ResponseEntity.class)
    @GetMapping(value = "/applied/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(@PathVariable Long id){
        return flutterService.getJobApplicantAllByApplicantByFlutter(id);
    }

    @ApiOperation(value = "flutter.getAllJobOfferByPublisher - Publisher: quiere ver todos sus avisos. El ID del publicador", response = ResponseEntity.class)
    @GetMapping(path = "/publisher/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllByPublisher(@PathVariable Long id) {
        return flutterService.getJobOfferAllByPublisher(id);
    }

    @ApiOperation(value = "flutter.getJobApplicantAllByJobOfferSimplePublisher - Publisher: Ver quien se aplico en cada aviso. El Id es el del aviso a consultar", response = ResponseEntity.class)
    @GetMapping(value = "/jobapplicants-by-my-offers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllAppliedByJobOffer(@PathVariable Long id){
        return flutterService.getAllAppliedByJobOffer(id);
    }

}
