package com.utn.bolsadetrabajo.controller.interfaces;

import com.utn.bolsadetrabajo.exception.PersonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Controllers<T> {
    ResponseEntity<?> get(@PathVariable Long id);
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid T entity) throws PersonException;
    ResponseEntity<?> delete(@PathVariable Long id);
    ResponseEntity<?> getAll();
}
