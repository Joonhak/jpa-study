package io.joonak.error;

import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.delivery.exception.DeliveryAlreadyCompletedException;
import io.joonak.delivery.exception.DeliveryNotFoundException;
import io.joonak.delivery.exception.DeliveryStatusEqualsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        final var accountNotFound = ErrorCode.ACCOUNT_NOT_FOUND;
        log.error(accountNotFound.getMessage(), e.getId());
        return bindError(accountNotFound);
    }

    @ExceptionHandler(EmailDuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleEmailDuplicationException(EmailDuplicationException e) {
        final var emailDuplication = ErrorCode.EMAIL_DUPLICATION;
        log.error(emailDuplication.getMessage(), e.getEmail(), e.getField());
        return bindError(emailDuplication);
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleDeliveryNotFoundException(DeliveryNotFoundException e) {
        final var deliveryNotFound = ErrorCode.DELIVERY_NOT_FOUND;
        log.error(deliveryNotFound.getMessage(), e.getId());
        return bindError(deliveryNotFound);
    }

    @ExceptionHandler(DeliveryStatusEqualsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleDeliveryStatusEqualsException(DeliveryStatusEqualsException e) {
        final var deliveryStatusEquals = ErrorCode.DELIVERY_STATUS_EQUALS;
        log.error(deliveryStatusEquals.getMessage(), e.getStatus());
        return bindError(deliveryStatusEquals);
    }

    @ExceptionHandler(DeliveryAlreadyCompletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleDeliveryAlreayCompletedException(DeliveryAlreadyCompletedException e) {
        final var deliveryAlreayCompleted = ErrorCode.DELIVERY_ALREADY_COMPLETED;
        log.error(deliveryAlreayCompleted.getMessage(), e.getId());
        return bindError(deliveryAlreayCompleted);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handelBindException(BindException e) {
        final var fieldErrors = getFieldErrors(e.getBindingResult());
        return bindErrorWithFieldErrors(fieldErrors);
    }

    // 유효하지 않은 SQL 및 Database 무결성 조건을 위반할 경우 발생하는 Exception을 Spring이 DataIntegrityViolationException 으로 변환시킨다.
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return bindError(ErrorCode.KEY_DUPLICATION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        final var bindingResult = e.getBindingResult();
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return bindErrorWithFieldErrors(
                errors.parallelStream()
                        .map(err -> ErrorResponse.FieldError.builder()
                                .field(err.getField())
                                .value((String) err.getRejectedValue())
                                .reason(err.getDefaultMessage())
                                .build())
                        .collect(toList())
        );
    }

    private ErrorResponse bindError(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .build();
    }

    private ErrorResponse bindErrorWithFieldErrors(List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .code(ErrorCode.INPUT_VALUE_INVALID.getCode())
                .message(ErrorCode.INPUT_VALUE_INVALID.getMessage())
                .status(ErrorCode.INPUT_VALUE_INVALID.getStatus())
                .errors(errors)
                .build();
    }

    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.parallelStream()
                .map(err -> ErrorResponse.FieldError.builder()
                        .field(err.getField())
                        .reason(err.getDefaultMessage())
                        .value((String) err.getRejectedValue())
                        .build())
                .collect(toList());
    }

}
