package com.nubo.masterparticipant.api.exception;

import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceExceptionTest {

    private ErrorCode errorCodeMock;

    @BeforeEach
    public void setUp() {
        errorCodeMock = mock(ErrorCode.class);
        when(errorCodeMock.getMessage()).thenReturn("Error message");
    }

    @Test
    @DisplayName("ServiceException With ErrorCode")
    void serviceExceptionWithErrorCodeTest() {
        ServiceException exception = new ServiceException(errorCodeMock);

        assertEquals("Error message", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatusOrDefault());
        assertEquals(errorCodeMock, exception.getErrorCode());
    }

    @Test
    @DisplayName("ServiceException With ErrorCode And HttpStatus")
    void serviceExceptionWithErrorCodeAndHttpStatusTest() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ServiceException exception = new ServiceException(errorCodeMock, httpStatus);

        assertEquals("Error message", exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatusOrDefault());
        assertEquals(errorCodeMock, exception.getErrorCode());
    }

    @Test
    @DisplayName("ServiceException With ErrorCode And Throwable")
    void serviceExceptionWithErrorCodeAndThrowableTest() {
        Throwable cause = new Throwable("Cause of the error");
        ServiceException exception = new ServiceException(errorCodeMock, cause);

        assertEquals("Error message", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatusOrDefault());
        assertEquals(cause, exception.getCause());
        assertEquals(errorCodeMock, exception.getErrorCode());
    }

    @Test
    @DisplayName("ServiceException With ErrorCode HttpStatus And Throwable")
    void serviceExceptionWithErrorCodeHttpStatusAndThrowableTest() {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Throwable cause = new Throwable("Cause of the error");
        ServiceException exception = new ServiceException(errorCodeMock, httpStatus, cause);

        assertEquals("Error message", exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatusOrDefault());
        assertEquals(cause, exception.getCause());
        assertEquals(errorCodeMock, exception.getErrorCode());
    }

    @Test
    @DisplayName("Get HttpStatus Or Default")
    void getHttpStatusOrDefaultTest() {
        ServiceException exceptionWithDefaultStatus = new ServiceException(errorCodeMock);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionWithDefaultStatus.getHttpStatusOrDefault());

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ServiceException exceptionWithCustomStatus = new ServiceException(errorCodeMock, httpStatus);
        assertEquals(httpStatus, exceptionWithCustomStatus.getHttpStatusOrDefault());
    }

    @Test
    @DisplayName("Get ErrorCode")
    void getErrorCodeTest() {
        ServiceException exception = new ServiceException(errorCodeMock);
        assertEquals(errorCodeMock, exception.getErrorCode());
    }

}