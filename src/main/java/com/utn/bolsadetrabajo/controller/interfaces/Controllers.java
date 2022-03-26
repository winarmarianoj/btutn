package com.utn.bolsadetrabajo.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Controllers<T> {
    ResponseEntity<?> getById(@PathVariable Long id);
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid T entity);
    ResponseEntity<?> delete(@PathVariable Long id);
    ResponseEntity<?> create(@RequestBody @Valid T entity) throws Exception;
    ResponseEntity<?> getAll();
}
