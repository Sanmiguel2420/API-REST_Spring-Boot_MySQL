package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CocheModelNotFoundException extends CocheModelException{
    public CocheModelNotFoundException(String mensaje){
        super(mensaje);
    }
}
