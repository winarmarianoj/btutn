package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.CategoryDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseSearchCategoryDto;
import com.utn.bolsadetrabajo.exception.CategoryException;
import com.utn.bolsadetrabajo.mapper.CategoryMapper;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.repository.CategoryRepository;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.service.interfaces.CategoryService;
import com.utn.bolsadetrabajo.util.Errors;
import com.utn.bolsadetrabajo.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CategoryMapper categoryMapper;
    @Autowired private MessageSource messageSource;
    @Autowired private ParametersRepository parametersRepository;
    @Autowired private Validator validCategory;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> getById(Long id) {
        try{
            Category category = categoryRepository.getById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryMapper.toResponsePerson(category, messageSource.getMessage("category.response.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.response.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("category.response.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.response.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, CategoryDTO categoryDTO) {
        return id > 0L ? updateCategory(id, categoryDTO) : create(categoryDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Category category = getCategory(id);
            category.setDeleted(true);
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("category.delete.success", new Object[] {id},null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.delete.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("category.delete.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.delete.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        try{
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toCategoriesList(categories));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            errors.logError(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.lists.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getFiltersAllCategories() {
        try{
            List<Category> categories = categoryRepository.findAll();
            List<ResponseSearchCategoryDto> lists = categoryMapper.toCategoriesListForFilters(categories);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            errors.logError(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.lists.failed", null, null));
        }
    }

    private ResponseEntity<?> updateCategory(Long id, CategoryDTO categoryDTO) {
        try{
            Category newCategory = categoryMapper.toModelUpdate(getCategory(id), categoryDTO);
            validCategory.validCategory(newCategory);
            Category aux = categoryRepository.save(newCategory);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toResponsePerson(aux, messageSource.getMessage("category.update.success", null,null)));
        }catch (CategoryException e){
            LOGGER.error(messageSource.getMessage("category.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("category.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("category.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    private ResponseEntity<?> create(CategoryDTO categoryDTO) {
        try{
            Category category = categoryMapper.toModel(categoryDTO);
            validCategory.validCategory(category);
            Category newCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponsePerson(newCategory, messageSource.getMessage("category.created.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("category.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("category.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

}
