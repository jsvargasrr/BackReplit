package com.nubo.masterparticipant.api.exception.handler;

import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.exception.error.ErrorResponse;
import com.nubo.masterparticipant.api.exception.error.ErrorValidation;
import com.nubo.masterparticipant.api.models.entity.LogEventEntity;
import com.nubo.masterparticipant.api.models.entity.UserNuboEntity;
import com.nubo.masterparticipant.api.repository.LogEventRepository;
import com.nubo.masterparticipant.api.repository.UserNuboRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogEventRepository logEventRepository;
    private final UserNuboRepository userNuboRepository;
    private final PropertiesConfig propertiesConfig;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerGeneralException(Exception exception,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request, ErrorCode.UNEXPECTED);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handlerServiceException(ServiceException exception,
                                                                 HttpServletRequest request) {
        HttpStatus httpStatus = exception.getHttpStatus();
        return buildErrorResponse(httpStatus, exception, request, exception.getErrorCode());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNoResourceFoundException(NoResourceFoundException exception,
                                                                         HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception, request, ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                                       HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, exception, request, ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                                                    HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception, request, ErrorCode.METHOD_ARGUMENT_TYPE);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception,
                                                                                   HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception, request, ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                                HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception, request, ErrorCode.MESSAGE_NOT_READABLE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handlerAccessDeniedException(AccessDeniedException exception,
                                                                      HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception, request, ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ErrorResponse> handlerInvalidBearerTokenException(InvalidBearerTokenException exception,
                                                                            HttpServletRequest request) {
        this.logoutUser();
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception, request, ErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                                HttpServletRequest request) {
        List<ErrorValidation> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorValidation.builder()
                        .id(error.getField())
                        .description(error.getDefaultMessage())
                        .build())
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception, request, ErrorCode.BAD_REQUEST, errors);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception exception,
                                                             HttpServletRequest request, ErrorCode errorCode) {
        return buildErrorResponse(status, exception, request, errorCode, null);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception exception,
                                                             HttpServletRequest request, ErrorCode errorCode,
                                                             List<ErrorValidation> errorDetails) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpCode(String.valueOf(status.value()))
                .httpMethod(request.getMethod())
                .url(request.getRequestURL().toString())
                .errorCode(errorCode.getName())
                .errorMessage(errorCode.getMessage())
                .errorMessageDetail(ObjectUtils.isEmpty(exception.getCause()) ?
                        exception.getMessage() : exception.getCause().getMessage())
                .timestamp(String.valueOf(PropertiesContext.getClientDatetime()))
                .errorDetails(errorDetails)
                .build();

        log.error("Error -> {}", errorResponse);

        this.createLogEvent(errorResponse);

        return ResponseEntity.status(status).body(errorResponse);
    }

    private void createLogEvent(ErrorResponse errorResponse) {
        log.info("Start createLogEvent");
        try {
            if (!ObjectUtils.isEmpty(PropertiesContext.getClientIp()) &&
                    !ObjectUtils.isEmpty(PropertiesContext.getClientUser()) &&
                    !ObjectUtils.isEmpty(PropertiesContext.getClientDatetime())) {
                String description = errorResponse.getErrorMessage();
                if (!ObjectUtils.isEmpty(errorResponse.getErrorMessageDetail()) &&
                        !(errorResponse.getErrorMessage().equals(errorResponse.getErrorMessageDetail()))) {
                    description = description.concat(" - ").concat(errorResponse.getErrorMessageDetail());
                }
                LogEventEntity logEventEntityLast = logEventRepository.findAll()
                        .stream()
                        .max(Comparator.comparingLong(LogEventEntity::getId))
                        .orElse(null);
                Long idLog = logEventEntityLast != null ? logEventEntityLast.getId() : 0;
                UUID uuid = UUID.nameUUIDFromBytes((idLog + UUID.randomUUID().toString()).getBytes());
                LogEventEntity logEventEntity = LogEventEntity.builder()
                        .number(Integer.parseInt(errorResponse.getHttpCode()))
                        .description(description)
                        .uuid(uuid.toString())
                        .origin(errorResponse.getUrl())
                        .tracking(PropertiesContext.getClientDatetime())
                        .lastModifyApplication(propertiesConfig.getApiName())
                        .lastModifyIp(PropertiesContext.getClientIp())
                        .lastModifyUser(PropertiesContext.getClientUser())
                        .lastModifyDate(PropertiesContext.getClientDatetime())
                        .build();
                logEventRepository.save(logEventEntity);

                log.info("End createLogEvent -> {}", logEventEntity);
            }
        } catch (Exception ex) {
            log.error("Error createLogEvent -> {}", ex.getMessage());
        }
    }

    private void logoutUser() {
        log.info("Start logoutUser, user");
        try {
            Optional<UserNuboEntity> userNuboEntityOptional = userNuboRepository.findByUserName(PropertiesContext.getClientUser());
            if (userNuboEntityOptional.isPresent()) {
                UserNuboEntity userNuboEntityLast = userNuboEntityOptional.get();
                userNuboEntityLast.setPreviousSession(false);
                userNuboRepository.save(userNuboEntityLast);

                log.info("End logoutUser -> {}", userNuboEntityLast);
            }
        } catch (Exception ex) {
            log.error("Error logoutUser -> {}", ex.getMessage());
        }
    }

}


