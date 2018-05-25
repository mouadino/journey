package com.journey.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.journey.domain.journey.JourneyNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler {

  static final private Logger logger = LoggerFactory.getLogger(BaseController.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request) {
      
      List<String> errors = new ArrayList<String>();
      for (FieldError error : ex.getBindingResult().getFieldErrors()) {
          errors.add(error.getField() + ": " + error.getDefaultMessage());
      }
      for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
          errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
      }

      logger.debug("invalid argument in handler: %s", ex);
      
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
      return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
    HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // FIXME: How to get human readable messages?
        String message = ex.getLocalizedMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
    MethodArgumentTypeMismatchException ex, WebRequest request) {
      String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

      logger.debug("invalid argument types in handler: %s", ex);
  
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
      return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity<Object> handleConstraintViolation(
    ConstraintViolationException ex, WebRequest request) {
      List<String> errors = new ArrayList<String>();
      for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
          errors.add(violation.getRootBeanClass().getName() + " " + 
            violation.getPropertyPath() + ": " + violation.getMessage());
      }
  
      logger.debug("contraint violation in handler: %s", ex);

      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
      return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(JourneyNotFoundException.class)
  public ResponseEntity<Object> resourceNotFound(JourneyNotFoundException ex) {
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, Collections.emptyList());
      return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
      ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred");

      logger.debug("unknwown error in handler: %s", ex);

      return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }
}