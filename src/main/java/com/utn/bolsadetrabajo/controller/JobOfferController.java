package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Controllers;
import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.service.JobOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/joboffer")
public class JobOfferController implements Controllers<JobOfferDTO> {

    private JobOfferService service;

    @Autowired
    public JobOfferController(JobOfferService service) {
        this.service = service;
    }

    @Override
    @ApiOperation(value = "joboffer.getById - Devuelve un JobOffer por su ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return service.getJobOfferById(id);
    }

    @Override
    @ApiOperation(value = "joboffer.update - Modifica un JobOffer por su ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid JobOfferDTO jobOfferDTO){
        return service.update(id, jobOfferDTO);
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
        return service.delete(id);
    }

    @Override
    @ApiOperation(value = "joboffer.create - Crea un JobOffer nuevo", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid JobOfferDTO jobOfferDTO)  {
        return service.save(jobOfferDTO);
    }

    @Override
    @ApiOperation(value = "joboffer.getAll - Devuelve la lista de Todos los JobOffer.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(name = "page",defaultValue = "0") int page) {
        return service.getAll(page);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllWithoutPage() {
        return service.getAllWithoutPage();
    }

    @ApiOperation(value = "joboffer.getAll - Devuelve la lista de los JobOffer segun el state solicitado.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/filter/{filter}")
    public ResponseEntity<?> getAllByFilter(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam State state){
        return service.getAllWithFilter(page, state);
    }

    @ApiOperation(value = "joboffer.applicantPostulate - Un Applicant se postula a un aviso a traves del ID.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping(value = "/postulate-{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> applicantPostulate(@PathVariable Long id){
        return service.postulate(id);
    }

    @ApiOperation(value = "joboffer.getEvaluationAllJobOffers - Evaluacion de cada aviso antes de ser publicado.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping("/evaluation")
    public ResponseEntity<?> getEvaluationAllJobOffers(@RequestBody @Valid JobOfferEvaluationDTO jobOfferEvaluationDTO){
        return service.getEvaluationAllJobOffers(jobOfferEvaluationDTO);
    }

    @ApiOperation(value = "joboffer.getJobApplicantAllByApplicant - Lo usa el applicante para ver su lista de postulaciones.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping(value = "/jobapplicants", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicant(@RequestParam(name = "page",defaultValue = "0") int page){
        return service.getJobApplicantAllByApplicant(page);
    }

    @ApiOperation(value = "joboffer.getJobApplicantAllByJobOfferSimplePublisher - Lo usa el publicador para ver quien se aplico en cada aviso.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping(value = "/jobapplicants-{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> jobApplicantAllByJobOfferSimplePublisher(@RequestParam(name = "page",defaultValue = "0") int page, @PathVariable Long id){
        return service.getJobApplicantAllByJobOfferSimplePublisher(page, id);
    }

    //Todo a revisar.....
    @ApiOperation(value = "joboffer.getJobApplicantAllByJobOfferSimplePublisher - Publicador quiere ver sus avisos por algun filtro de Categoria.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping(path = "/publisher-{filter}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllJobOfferSimplePublisherByFilter(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam Category filter) {
        return service.getAllJobOfferSimplePublisher(page, filter);
    }
}
