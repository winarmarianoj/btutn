package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Controllers;
import com.utn.bolsadetrabajo.dto.request.PublisherDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.service.PublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "Publisher Controller", description = "Controlador con los endpoints que actúan sobre los Publisher.")
@RequestMapping("/publisher")
public class PublisherController implements Controllers<PublisherDTO> {

    private PublisherService service;
    private MessageSource messageSource;

    @Autowired
    public PublisherController(PublisherService publisherServiceImpl, MessageSource messageSource) {
        this.service = publisherServiceImpl;
        this.messageSource = messageSource;
    }

    @ApiOperation(value = "${publisher.getById} - Devuelve un Publisher por su ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return service.getPublisherById(id);
    }

    @ApiOperation(value = "${publisher.getPublisher} - Devuelve un Publisher por su Cuit", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<?> getPublisher(@PathVariable String cuit){
        return service.getPublisherByCuit(cuit);
    }

    @ApiOperation(value = "${publisher.update} - Modifica un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PublisherDTO publisherDTO){
        return service.update(id, publisherDTO);
    }

    @ApiOperation(value = "${publisher.delete} - Elimina un objeto Publisher", response = ResponseEntity.class)
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

    @ApiOperation(value = "${publisher.create} - Crea un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PublisherDTO publisherDTO) throws PersonException {
        return service.save(publisherDTO);
    }

    @ApiOperation(value = "${publisher.getAll} - Devuelve a todos los Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAll(@RequestParam(name = "page",defaultValue = "0") int page){
        return service.getAllPublishers(page);
    }

}
