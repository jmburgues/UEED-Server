package edu.utn.UEEDServer.exceptions;

import lombok.Data;

@Data
public class IDnotFoundException extends RuntimeException {

    public IDnotFoundException(String entity, String value) {
        super(entity + " " + value + " not found.");
    }
}