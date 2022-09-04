package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.CategoryDTO;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

public interface CategoryService extends Removable, Writeable<CategoryDTO> {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> getFiltersAllCategories();
}
