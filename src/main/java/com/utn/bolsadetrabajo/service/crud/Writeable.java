package com.utn.bolsadetrabajo.service.crud;

import com.utn.bolsadetrabajo.exception.PersonException;
import org.springframework.http.ResponseEntity;

public interface Writeable<T> {
    int ZERO = 0;

    ResponseEntity<?> update(Long id, T entity) throws PersonException;
}
