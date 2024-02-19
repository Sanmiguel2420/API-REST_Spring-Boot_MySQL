package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CocheModelBadRequestException extends CocheModelException{
    public CocheModelBadRequestException(String mensaje){
        super(mensaje);
    }
}
