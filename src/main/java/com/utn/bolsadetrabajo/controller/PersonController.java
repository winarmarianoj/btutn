package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Controllers;
import com.utn.bolsadetrabajo.controller.interfaces.Creators;
import com.utn.bolsadetrabajo.controller.interfaces.Messages;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.service.crud.Readable;
import com.utn.bolsadetrabajo.service.interfaces.PersonService;
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
@Api(value = "Person Controller", description = "Controlador con los endpoints que actúan sobre las Person.")
@RequestMapping("/person")
public class PersonController implements Controllers<PersonDTO>, Messages, Creators<PersonDTO> {

    @Autowired Readable readableService;
    @Autowired PersonService personService;

    @Override
    @ApiOperation(value = "${person.getById} - Devuelve los datos de una persona por su ID", response = ResponseEntity.class)
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
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return readableService.getById(id);
    }

    @ApiOperation(value = "${person.getByDni} - Devuelve los datos de una persona por su DNI", response = ResponseEntity.class)
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
    @GetMapping("/identification/{identification}")
    public ResponseEntity<?> getByDni(@PathVariable String identification){
        return readableService.getByIdentification(identification);
    }

    /**
     * Devuelve un Publisher a traves del id del User
     * @param id del user
     * @return objeto publisher para mostrar en profile
     */
    @GetMapping(path = "/userId/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getByIdUser(@PathVariable Long id){
        return readableService.getByIdUser(id);
    }

    @Override
    @ApiOperation(value = "${person.update} - Modifica o crea una persona", response = ResponseEntity.class)
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PersonDTO personDTO) throws PersonException {
        return personService.update(id, personDTO);
    }

    @Override
    @ApiOperation(value = "${person.delete} - Elimina una persona con Baja lògica", response = ResponseEntity.class)
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
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable Long id){
        return personService.delete(id);
    }

    @Override
    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return personService.getAll();
    }

    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping("/applicants")
    public ResponseEntity<?> getAllApplicant(@RequestParam(name = "page",defaultValue = "0") int page) {
        return personService.getAllApplicant(page);
    }

    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping("/publishers")
    public ResponseEntity<?> getAllPublisher(@RequestParam(name = "page",defaultValue = "0") int page) {
        return personService.getAllPublisher(page);
    }

    @Override
    @ApiOperation(value = "${person.create} - Crea una Persona nueva", response = ResponseEntity.class)
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
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO personDTO) throws PersonException {
        return personService.update(0L, personDTO);
    }

}
