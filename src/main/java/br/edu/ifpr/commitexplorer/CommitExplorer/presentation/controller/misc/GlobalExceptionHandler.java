package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller.misc;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        log.warn("Erro de validação: {}", errors);
        return ResponseEntity.badRequest().body(ResponseBuilder.validationError(errors));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<Object>> handleBindExceptions(BindException ex) {
        List<String> errors = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Erro de bind: {}", errors);
        return ResponseEntity.badRequest().body(ResponseBuilder.validationError(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        log.warn("Violação de constraint: {}", errors);
        return ResponseEntity.badRequest().body(ResponseBuilder.validationError(errors));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Entidade não encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseBuilder.notFound(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Credenciais inválidas: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseBuilder.unauthorized());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseBuilder.forbidden());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Object>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String message = "Parâmetro obrigatório ausente: " + ex.getParameterName();
        log.warn(message);
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.error(message, "MISSING_PARAMETER"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Tipo de parâmetro inválido para '" + ex.getName() + "'. Esperado: " +
                        ex.getRequiredType().getSimpleName();
        log.warn(message);
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.error(message, "INVALID_PARAMETER_TYPE"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("JSON malformado: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.error("Formato JSON inválido", "INVALID_JSON"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Object>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = "Método HTTP '" + ex.getMethod() + "' não é suportado para esta URL";
        log.warn(message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ResponseBuilder.error(message, "METHOD_NOT_ALLOWED"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = "Endpoint não encontrado: " + ex.getHttpMethod() + " " + ex.getRequestURL();
        log.warn(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseBuilder.error(message, "ENDPOINT_NOT_FOUND"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Argumento ilegal: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ResponseBuilder.error(ex.getMessage(), "ILLEGAL_ARGUMENT"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {
        log.error("Erro interno não tratado: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseBuilder.internalError());
    }
}
