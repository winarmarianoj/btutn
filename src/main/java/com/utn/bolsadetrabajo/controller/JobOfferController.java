package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Controllers;
import com.utn.bolsadetrabajo.controller.interfaces.Messages;
import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.request.PostulateDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.service.interfaces.JobOfferService;
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
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/joboffer")
public class JobOfferController implements Controllers<JobOfferDTO>, Messages {

    @Autowired private JobOfferService jobOfferService;

    @Override
    @ApiOperation(value = "joboffer.getById - Devuelve un JobOffer por su ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return jobOfferService.getJobOfferById(id);
    }

    @Override
    @ApiOperation(value = "joboffer.update - Modifica o crea un JobOffer " +
            "ID del User del Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid JobOfferDTO jobOfferDTO) throws PersonException {
        return jobOfferService.update(id, jobOfferDTO);
    }

    @Override
    @ApiOperation(value = "joboffer.delete - Elimina un JobOffer por su ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable Long id){
        return jobOfferService.delete(id);
    }

    @ApiOperation(value = "joboffer.create - Crea un JobOffer nuevo", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createJob(@PathVariable Long id, @RequestBody @Valid JobOfferDTO jobOfferDTO) throws PersonException {
        return jobOfferService.update(id, jobOfferDTO);
    }

    @Override
    @ApiOperation(value = "joboffer.getAll - Devuelve la lista de Todos los JobOffer Sin Page.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return jobOfferService.getAll();
    }

    @ApiOperation(value = "joboffer.applicantPostulate - Un Applicant se postula a un aviso a traves del ID.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/postulate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> postulate(@RequestBody @Valid PostulateDTO postulateDTO){
        return jobOfferService.postulate(postulateDTO);
    }

    @ApiOperation(value = "report-lists.getEvaluationAllJobOffers -UTN: Es la respuesta de UTN" +
            "Evaluacion de sobre cada aviso antes de ser publicado.", response = ResponseEntity.class)
    @PostMapping("/evaluation")
    public ResponseEntity<?> getJobOfferAllEvaluation(@RequestBody @Valid JobOfferEvaluationDTO jobOfferEvaluationDTO){
        return jobOfferService.getJobOfferAllEvaluation(jobOfferEvaluationDTO);
    }


}
