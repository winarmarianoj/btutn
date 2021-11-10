package com.utn.bolsadetrabajo.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface Controllers<T> {
    String OK_RESPONSE = "Operacion realizada con exito.";
    String CREATED_RESPONSE = "Creado con exito.";
    String UNAUTHORIZED_RESPONSE = "Necesita autorizacion para realizar esta operacion..";
    String FORBIDDEN_RESPONSE = "No tiene los permisos necesarios para realizar esta operacion.";
    String NOT_FOUND_RESPONSE = "El recurso buscado no existe o no se encuentra disponible.";

    ResponseEntity<?> getById(@PathVariable Long id);
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid T entity);
    ResponseEntity<?> delete(@PathVariable Long id);
    ResponseEntity<?> create(@RequestBody @Valid T entity) throws Exception;
    ResponseEntity<?> getAll(@RequestParam(name = "page",defaultValue = "0") int page);
}
