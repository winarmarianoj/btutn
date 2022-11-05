package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.service.interfaces.FlutterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/flutter/reports")
public class FlutterController {

    @Autowired FlutterService flutterService;

    @ApiOperation(value = "report-lists.getJobApplicantAllByApplicantByFlutter - Applicant: Ver su lista de postulaciones. El ID que se recibe es el del Applicant quien consulta.", response = ResponseEntity.class)
    @GetMapping(value = "/applied/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(@PathVariable Long id){
        return flutterService.getJobApplicantAllByApplicantByFlutter(id);
    }


}
