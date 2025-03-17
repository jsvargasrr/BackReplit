package com.nubo.masterparticipant.api.exception.handler;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.util.ApplicationUtils;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.exception.error.ErrorResponse;
import com.nubo.masterparticipant.api.exception.error.ErrorValidation;
import com.nubo.masterparticipant.api.models.entity.LogEventEntity;
import com.nubo.masterparticipant.api.models.entity.UserNuboEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.repository.LogEventRepository;
import com.nubo.masterparticipant.api.repository.UserNuboRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Mock
    private LogEventRepository logEventRepository;

    @Mock
    private UserNuboRepository userNuboRepository;

    @Mock
    private PropertiesConfig propertiesConfig;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));
        GlobalContext.setProperty(PropertiesEnum.CLIENT_IP.getValue(), "any string");
        GlobalContext.setProperty(PropertiesEnum.CLIENT_USER.getValue(), "any string");
        GlobalContext.setProperty(PropertiesEnum.CLIENT_DATETIME.getValue(), ApplicationUtils.getLocalDateTimeNow());
    }

    @Test
    @DisplayName("Handler GeneralException")
    void handlerGeneralExceptionTest() {
        when(logEventRepository.findAll()).thenReturn(List.of(createLogEventEntityTestData()));
        when(logEventRepository.save(any())).thenReturn(createLogEventEntityTestData());

        Exception exception = new Exception("General error");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerGeneralException(exception, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.UNEXPECTED.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.UNEXPECTED.getMessage(), errorResponse.getErrorMessage());
        assertEquals("General error", errorResponse.getErrorMessageDetail());
    }

    @Test
    @DisplayName("Handler ServiceException")
    void handlerServiceExceptionTest() {
        ServiceException exception = new ServiceException(ErrorCode.FORBIDDEN);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api"));
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerServiceException(exception, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("POST", errorResponse.getHttpMethod());
        assertEquals("http://localhost/api", errorResponse.getUrl());
        assertEquals(ErrorCode.FORBIDDEN.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.FORBIDDEN.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler NoResourceFoundException")
    void handlerNoResourceFoundExceptionTest() {
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "path");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerNoResourceFoundException(exception, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.NOT_FOUND.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.NOT_FOUND.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler HttpRequestMethodNotSupportedException")
    void handlerHttpRequestMethodNotSupportedExceptionTest() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerHttpRequestMethodNotSupportedException(exception, request);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.METHOD_NOT_ALLOWED.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.METHOD_NOT_ALLOWED.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler MethodArgumentTypeMismatchException")
    void handlerMethodArgumentTypeMismatchExceptionTest() {
        MethodParameter methodParameter = mock(MethodParameter.class);
        doReturn(String.class).when(methodParameter).getParameterType();

        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "value", String.class, "name", methodParameter, null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerMethodArgumentTypeMismatchException(exception, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.METHOD_ARGUMENT_TYPE.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.METHOD_ARGUMENT_TYPE.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler HttpMediaTypeNotSupportedException")
    void handlerHttpMediaTypeNotSupportedExceptionTest() {
        HttpMediaTypeNotSupportedException exception = new HttpMediaTypeNotSupportedException("application/xml");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerHttpMediaTypeNotSupportedException(exception, request);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler HttpMessageNotReadableException")
    void handlerHttpMessageNotReadableExceptionTest() {
        HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "Message not readable", httpInputMessage);
        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerHttpMessageNotReadableException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.MESSAGE_NOT_READABLE.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.MESSAGE_NOT_READABLE.getMessage(), errorResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Handler AccessDeniedException")
    void handlerAccessDeniedExceptionTest() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerAccessDeniedException(exception, request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.FORBIDDEN.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.FORBIDDEN.getMessage(), errorResponse.getErrorMessage());
        assertEquals("Access denied", errorResponse.getErrorMessageDetail());
    }

    @Test
    @DisplayName("Handler InvalidBearerTokenException")
    void handlerInvalidBearerTokenExceptionTest() {
        when(userNuboRepository.findByUserName(any())).thenReturn(Optional.ofNullable(createUserNuboEntityTestData()));
        when(userNuboRepository.save(any(UserNuboEntity.class))).thenReturn(createUserNuboEntityTestData());

        InvalidBearerTokenException exception = new InvalidBearerTokenException("Invalid token");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerInvalidBearerTokenException(exception, request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.UNAUTHORIZED.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.UNAUTHORIZED.getMessage(), errorResponse.getErrorMessage());
        assertEquals("Invalid token", errorResponse.getErrorMessageDetail());
    }

    @Test
    @DisplayName("Handler MethodArgumentNotValidException")
    void handlerMethodArgumentNotValidExceptionTest() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(
                        new FieldError("object", "field1", "error1"),
                        new FieldError("object", "field2", "error2")
                )
        );

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerMethodArgumentNotValidException(exception, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("GET", errorResponse.getHttpMethod());
        assertEquals("http://localhost", errorResponse.getUrl());
        assertEquals(ErrorCode.BAD_REQUEST.getName(), errorResponse.getErrorCode());
        assertEquals(ErrorCode.BAD_REQUEST.getMessage(), errorResponse.getErrorMessage());

        List<ErrorValidation> details = errorResponse.getErrorDetails();
        assertNotNull(details);
        assertEquals(2, details.size());
        assertEquals("field1", details.get(0).getId());
        assertEquals("error1", details.get(0).getDescription());
        assertEquals("field2", details.get(1).getId());
        assertEquals("error2", details.get(1).getDescription());
    }

    private LogEventEntity createLogEventEntityTestData() {
        String anyString = "any string";
        return LogEventEntity.builder()
                .id(1L)
                .number(0)
                .description(anyString)
                .uuid(UUID.randomUUID().toString())
                .origin(anyString)
                .tracking(LocalDateTime.now())
                .build();
    }

    private UserNuboEntity createUserNuboEntityTestData() {
        String anyString = "any string";
        return UserNuboEntity.builder()
                .id(1L)
                .userName(anyString)
                .build();
    }

}
