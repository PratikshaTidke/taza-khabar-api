package com.project.www.news_aggregator_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(NewsApiException.class)
	    public ResponseEntity<Map<String, String>> handleNewsApiException(NewsApiException ex) {
	        Map<String, String> errorResponse = Map.of(
	            "message", ex.getMessage()
	        );
	        // Send a 503 Service Unavailable status
	        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
	    }

	    @ExceptionHandler(Exception.class) // A fallback for any other unexpected errors
	    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
	        Map<String, String> errorResponse = Map.of(
	            "message", "An unexpected error occurred. Please try again later."
	        );
	        // Send a 500 Internal Server Error status
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

}
