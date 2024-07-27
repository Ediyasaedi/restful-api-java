package yassa_exercise.restful_api.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import yassa_exercise.restful_api.models.ResponseData;
import yassa_exercise.restful_api.models.WebResponse;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<Object>> constraintViolationException(ConstraintViolationException cve) {
        ResponseData responseData = new ResponseData();
        responseData.setErrors(cve.getMessage());
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WebResponse.builder().success(false).data(responseData).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<Object>> responseStatusException(ResponseStatusException rse) {
        ResponseData responseData = new ResponseData();
        responseData.setErrors(rse.getReason());
        return ResponseEntity.status(rse.getStatusCode()).body(WebResponse.builder().success(false).data(responseData).build());
    }
}
