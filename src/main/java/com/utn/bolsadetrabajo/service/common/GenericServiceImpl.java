package com.utn.bolsadetrabajo.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.ParameterizedType;

public abstract class GenericServiceImpl<I, O> implements GenericService<I, O> {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    @Autowired MessageSource messageSource;
    public Class<O> clazz;

    @SuppressWarnings("unchecked")
    public GenericServiceImpl() {
        this.clazz = ((Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    @Override
    public ResponseEntity<?> get(Long id){
        return null;
    }

    @Override
    public ResponseEntity<?> getByData(String data){
        return null;
    }

    @Override
    public ResponseEntity<?> update(Long id, O entity){
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Long id){
        return null;
    }

    @Override
    public ResponseEntity<?> save(I entity){
        return null;
    }

    @Override
    public ResponseEntity<?> getAll(int page){
        return null;
    }

}
