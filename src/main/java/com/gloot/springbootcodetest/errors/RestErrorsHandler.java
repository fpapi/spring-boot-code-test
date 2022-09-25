package com.gloot.springbootcodetest.errors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestErrorsHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleConflict(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ProblemDto.builder().status(HttpStatus.NOT_FOUND.value()).reason(ex.getMessage()).build());
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleConflict(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ProblemDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).reason(ex.getMessage()).build());
	}
}
