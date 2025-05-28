package com.internship.financeservice.utils.exceptions

import com.internship.financeservice.utils.exceptions.transfer.BaseException
import com.internship.financeservice.utils.exceptions.transfer.BaseValidationException
import lombok.RequiredArgsConstructor
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<BaseValidationException> {
        val errors = ex.bindingResult
            .fieldErrors
            .stream()
            .map { fieldError: FieldError ->
                val arguments = fieldError.arguments
                messageSource!!.getMessage(
                    fieldError.defaultMessage!!,
                    arguments,
                    LocaleContextHolder.getLocale()
                )
            }
            .toList()
        return ResponseEntity<BaseValidationException>(
            BaseValidationException(
                messageSource!!.getMessage(ExceptionCodes.ERROR_INVALID_INPUT.getCode(),
                    null,
                    LocaleContextHolder.getLocale()
                ), errors
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<BaseException> {
        return ResponseEntity<BaseException>(
            BaseException(
                messageSource!!.getMessage(
                    ex.message,
                    null,
                    LocaleContextHolder.getLocale()
                )
            ), HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInputExceptions(ex: Exception): ResponseEntity<BaseException> {
        return ResponseEntity<BaseException>(
            BaseException(
                messageSource!!.getMessage(
                    ex.message!!,
                    null,
                    LocaleContextHolder.getLocale()
                )
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException?): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                messageSource!!.getMessage(
                    ExceptionCodes.ERROR_NOT_READABLE.getCode(),
                    null,
                    LocaleContextHolder.getLocale()
                )
            )
    }
}
