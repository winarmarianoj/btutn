package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface CategoryService {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> update(Long id, CategoryDTO categoryDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> save(CategoryDTO categoryDTO);

    ResponseEntity<?> getAllCategories();

    ResponseEntity<?> getFiltersAllCategories();
}
