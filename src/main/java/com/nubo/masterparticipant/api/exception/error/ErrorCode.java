package com.nubo.masterparticipant.api.exception.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNAUTHORIZED("Not unauthorized"),
    FORBIDDEN("Access denied"),
    UNEXPECTED("An unexpected error occurred"),
    METHOD_ARGUMENT_TYPE("Invalid request path"),
    METHOD_NOT_ALLOWED("Http method not allowed"),
    NOT_FOUND("Resource not found"),
    UNSUPPORTED_MEDIA_TYPE("Content Type is not supported"),
    MESSAGE_NOT_READABLE("Error reading the message body request"),
    BAD_REQUEST("Invalid request body"),
    RATE_LIMITING_EXCEEDED("Rate Limiting Exceeded"),
    HEADER_CLIENT_IP_REQUIRED("Header client_ip required"),
    HEADER_CLIENT_IP_INVALID("Header client_ip is invalid"),
    HEADER_CLIENT_USER_REQUIRED("Header client_user is required"),
    HEADER_CLIENT_DATETIME_REQUIRED("Header client_datetime is required"),
    HEADER_CLIENT_DATETIME_INVALID("Header client_datetime is invalid"),
    VERSION_API_NOT_FOUND("Version API not Found"),
    PARTICIPANT_NOT_FOUND("Participant not Found"),
    COUNTRY_NOT_FOUND("Country not Found"),
    CURRENCY_NOT_FOUND("Currency not Found"),
    PARTICIPANT_ACCOUNT_NOT_FOUND("Participant Account not Found"),
    INVESTOR_NOT_FOUND("Investor not Found"),
    PARTICIPANT_INVESTOR_EXISTS("ParticipantInvestor already exists"),
    TRADE_NOT_FOUND("Trade not Found"),
    TYPE_NOT_FOUND("Type not Found"),
    DEPARTMENT_NOT_FOUND("Department not found"),
    STATUS_NOT_FOUND("Status not found"),
    IDENTIFICATION_TYPE_NOT_FOUND("Identification Type not found"),
    IDENTIFICATION_TYPE_NOT_FOUND_FOR_THE_COUNTRY("Identification Type not found for the country"),
    TAX_STATUS_NOT_FOUND("Tax Status not found");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

}
