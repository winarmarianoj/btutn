package com.utn.bolsadetrabajo.service.crud;

import org.springframework.http.ResponseEntity;

public interface Removable {
    ResponseEntity<?> delete(Long id);
}
