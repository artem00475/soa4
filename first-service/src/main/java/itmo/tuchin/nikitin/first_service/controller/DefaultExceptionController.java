package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.dto.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DefaultExceptionController {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleDefaultException(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage()));
    }
}
