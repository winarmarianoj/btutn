package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Creators;
import com.utn.bolsadetrabajo.controller.interfaces.Messages;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
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
@Api(value = "Publisher Controller", description = "Controlador con los endpoints que actúan sobre los Publisher.")
@RequestMapping("/publisher")
public class PublisherController implements Messages, Creators<PersonDTO> {

    @Autowired PublisherService publisherService;

    @ApiOperation(value = "${publisher.update} - Modifica un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE)
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PersonDTO publisherDTO) throws PersonException {
        return publisherService.update(id, publisherDTO);
    }

    @Override
    @ApiOperation(value = "${publisher.create} - Crea un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE)
    })
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO publisherDTO) throws PersonException {
        return publisherService.update(0L, publisherDTO);
    }
}

