package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String category);
}
