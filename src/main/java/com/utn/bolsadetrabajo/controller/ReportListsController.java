package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.service.reports.ReportLists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/report-lists")
public class ReportListsController {

    @Autowired ReportLists reportLists;

    @ApiOperation(value = "report-lists.getAllWithPage - Devuelve la lista de Todos los JobOffer Con Page.", response = ResponseEntity.class)
    @GetMapping("/with-page")
    public ResponseEntity<?> getAllWithPage(@RequestParam(name = "page",defaultValue = "0") int page) {
        return reportLists.getAllWithPage(page);
    }

    @ApiOperation(value = "report-lists.getJobApplicantAllByApplicant - Applicant: Ver su lista de postulaciones. El ID que se recibe es el del Applicant quien consulta.", response = ResponseEntity.class)
    @GetMapping(value = "/jobapplicants/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicant(@PathVariable Long id){
        return reportLists.getJobApplicantAllByApplicant(id);
    }

    @ApiOperation(value = "report-lists.getJobApplicantAllByJobOfferSimplePublisher - Publisher: Ver quien se aplico en cada aviso. El Id es el del aviso a consultar", response = ResponseEntity.class)
    @GetMapping(value = "/jobapplicants-by-my-offers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(@PathVariable Long id){
        return reportLists.getJobApplicantAllByJobOfferSimplePublisher(id);
    }

    @ApiOperation(value = "report-lists.getAllJobOfferByPublisher - Publisher: quiere ver todos sus avisos. El ID del publicador", response = ResponseEntity.class)
    @GetMapping(path = "/publisher/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllByPublisher(@PathVariable Long id) {
        return reportLists.getJobOfferAllByPublisher(id);
    }

    //Todo a revisar.....ya resuelto en el front
    @ApiOperation(value = "report-lists.getAllJobOfferSimplePublisherByFilter - Publicador quiere ver sus avisos por algun filtro de Categoria. El ID del publicador", response = ResponseEntity.class)
    @GetMapping(path = "/publisher/filter/{filter}/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllSimplePublisher(@RequestParam Category filter, @PathVariable Long id) {
        return reportLists.getJobOfferAllSimplePublisher(filter, id);
    }

    @ApiOperation(value = "report-lists.getJobOfferAllWithFilter - UTN: Devuelve la lista de los JobOffer segun el state solicitado.", response = ResponseEntity.class)
    @GetMapping(path = "/filter/{state}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllWithFilter(@PathVariable String state){
        return reportLists.getJobOfferAllWithFilter(state);
    }

    @ApiOperation(value = "report-lists.getEvaluationAllJobOffers -UTN: Es la respuesta de UTN" +
            "Evaluacion de sobre cada aviso antes de ser publicado.", response = ResponseEntity.class)
    @PostMapping("/evaluation")
    public ResponseEntity<?> getJobOfferAllEvaluation(@RequestBody @Valid JobOfferEvaluationDTO jobOfferEvaluationDTO){
        return reportLists.getJobOfferAllEvaluation(jobOfferEvaluationDTO);
    }



}
