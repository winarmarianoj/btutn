package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.CategoryController;
import com.utn.bolsadetrabajo.dto.request.CategoryDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseCategoryDto;
import com.utn.bolsadetrabajo.dto.response.ResponseSearchCategoryDto;
import com.utn.bolsadetrabajo.exception.CategoryException;
import com.utn.bolsadetrabajo.mapper.CategoryMapper;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.repository.CategoryRepository;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.service.CategoryService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private MessageSource messageSource;
    private ParametersRepository parametersRepository;
    private Validator validCategory;

    private List<Link> links;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, MessageSource messageSource, ParametersRepository parametersRepository, Validator validCategory) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.messageSource = messageSource;
        this.parametersRepository = parametersRepository;
        this.validCategory = validCategory;
    }

    @Override
    public ResponseEntity<?> getById(Long id) {
        try{
            Category category = categoryRepository.getById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    categoryMapper.toResponsePerson(category,
                            messageSource.getMessage("category.response.success", null,null))
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("category.response.failed",
                            new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, CategoryDTO categoryDTO) {
        try{
            Category category = categoryRepository.getById(id);
            Category newCategory = categoryMapper.toModelUpdate(category, categoryDTO);
            validCategory.validCategory(newCategory);
            Category aux = categoryRepository.save(newCategory);
            return ResponseEntity.status(HttpStatus.OK).body(
                    categoryMapper.toResponsePerson(aux,
                            messageSource.getMessage("category.update.success", null,null))
            );
        }catch (CategoryException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body(messageSource.getMessage("category.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Category category = categoryRepository.findById(id).get();
            category.setDeleted(true);
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body(
                    messageSource.getMessage("category.delete.success", new Object[] {id},null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("category.delete.failed",
                            new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> save(CategoryDTO categoryDTO) {
        try{
            Category category = categoryMapper.toModel(categoryDTO);
            validCategory.validCategory(category);
            Category newCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    categoryMapper.toResponsePerson(newCategory,
                            messageSource.getMessage("category.created.success", null,null)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("category.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllCategories(int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);

        List<Category> categories = categoryRepository.findAll();
        List<ResponseCategoryDto> lists = categoryMapper.toCategoriesList(categories);
        Page<ResponseCategoryDto> page = new PageImpl<>(lists, pageable, lists.size());

        links = new ArrayList<>();
        try{
            if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
            links.add(linkTo(methodOn(CategoryController.class).getAll(numberPage)).withSelfRel());

            if(page.hasPrevious()){
                links.add(linkTo(methodOn(CategoryController.class)
                        .getAll(numberPage - 1)).withRel("prev"));
            }
            if(page.hasNext()){
                links.add(linkTo(methodOn(CategoryController.class)
                        .getAll(numberPage + 1)).withRel("next"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("category.lists.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getFiltersAllCategories() {
        try{
            List<Category> categories = categoryRepository.findAll();
            List<ResponseSearchCategoryDto> lists = categoryMapper.toCategoriesListForFilters(categories);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("category.lists.failed",
                            null, null));
        }
    }

}
