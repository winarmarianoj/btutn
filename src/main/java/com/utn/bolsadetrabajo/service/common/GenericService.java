package com.utn.bolsadetrabajo.service.common;

import org.springframework.http.ResponseEntity;

public interface GenericService<I, O>{

    ResponseEntity<?> get(Long id);

    ResponseEntity<?> getByData(String data);

    ResponseEntity<?> update(Long id, O entity);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> save(I entity);

    ResponseEntity<?> getAll(int page);
}
