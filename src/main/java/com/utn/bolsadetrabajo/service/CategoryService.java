package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> update(Long id, CategoryDTO categoryDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> save(CategoryDTO categoryDTO);

    ResponseEntity<?> getAllCategories(int page);

    ResponseEntity<?> getFiltersAllCategories();
}
