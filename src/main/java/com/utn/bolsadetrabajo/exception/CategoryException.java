package com.utn.bolsadetrabajo.exception;

@SuppressWarnings("serial")
public class CategoryException extends Exception{

    protected int idError;
    protected String error;

    public CategoryException() {}

    public CategoryException(String message) {
        super(message);
    }

    public String getError(){
        return this.error;
    }

    protected void setError(String message){
        this.error = message;
    }
}
