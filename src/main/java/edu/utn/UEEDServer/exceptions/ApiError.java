package edu.utn.UEEDServer.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private HttpStatus httStatus;
    private String message;
    private List<String> errors;
}
