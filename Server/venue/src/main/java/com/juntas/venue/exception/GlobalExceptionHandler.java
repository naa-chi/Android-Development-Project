package com.juntas.venue.exception;

import com.juntas.venue.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//This segment got indented incorrectly. I'm not fixing this. I don't care.

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(VenueNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(VenueNotFoundException ex, HttpServletRequest req) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiError.of(404, "Not Found", ex.getMessage(), req.getRequestURI()));
        }

        @ExceptionHandler(VenueAlreadyExistsException.class)
        public ResponseEntity<ApiError> handleConflict(VenueAlreadyExistsException ex, HttpServletRequest req) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(ApiError.of(409, "Conflict", ex.getMessage(), req.getRequestURI()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                        HttpServletRequest req) {
                Map<String, String> fieldErrors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(fe ->
                        fieldErrors.put(fe.getField(), fe.getDefaultMessage())
                );
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.of(400, "Bad Request", "Validation failed",
                                        req.getRequestURI(), fieldErrors));
        }

        @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> handleUnreadable(
                org.springframework.http.converter.HttpMessageNotReadableException ex,
                HttpServletRequest req) {
                String msg = ex.getMessage() != null && ex.getMessage().contains("VenueType")
                        ? "Tipo de recinto inválido"
                        : "Cuerpo de la petición mal formado";
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.of(400, "Bad Request", msg, req.getRequestURI()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiError.of(500, "Internal Server Error",
                                        "Ha ocurrido un error inesperado",
                                        req.getRequestURI()));
        }
        @ExceptionHandler(NeighborhoodServiceUnavailableException.class)
                public ResponseEntity<ApiError> handleNeighborhoodDown(
                        NeighborhoodServiceUnavailableException ex, HttpServletRequest req) {
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(ApiError.of(503, "Service Unavailable", ex.getMessage(), req.getRequestURI()));
                }
                @ExceptionHandler(NeighborhoodNotFoundException.class)
                public ResponseEntity<ApiError> handleNeighborhoodNotFound(
                        NeighborhoodNotFoundException ex, HttpServletRequest req) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiError.of(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
                        }
}