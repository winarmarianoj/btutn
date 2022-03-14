package com.utn.bolsadetrabajo.service.common;

import com.utn.bolsadetrabajo.exception.PersonException;
import org.springframework.http.ResponseEntity;

public interface GenericService<T> {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getByIdentification(String identification);

    ResponseEntity<?> getByIdUser(Long id);

    ResponseEntity<?> update(Long id, T entity);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> save(T entity) throws PersonException;

}
