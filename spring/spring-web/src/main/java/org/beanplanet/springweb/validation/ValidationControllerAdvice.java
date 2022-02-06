package org.beanplanet.springweb.validation;

import org.beanplanet.messages.domain.Messages;
import org.beanplanet.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationControllerAdvice {
  @ExceptionHandler(value = {ValidationException.class})
  public ResponseEntity<Messages> handleValidationError(ValidationException ex) {
    return new ResponseEntity<>(ex.getMessages(), HttpStatus.BAD_REQUEST);
  }
}
