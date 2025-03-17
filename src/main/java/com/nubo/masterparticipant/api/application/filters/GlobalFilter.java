package com.nubo.masterparticipant.api.application.filters;

import com.google.gson.Gson;
import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.exception.error.ErrorResponse;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.UserNuboRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

@Component
@Order(1)
@Slf4j
public class GlobalFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();
            if (uri.contains("/v3/api-docs") || uri.contains("/swagger-ui")) {
                chain.doFilter(request, response);
                return;
            }
            String versionPath = this.validateVersionPath(request);
            String clientIpHeader = this.validateClientIpHeader(request);
            String clientUserHeader = this.validateClientUserHeader(request);
            String clientDatetime = this.validateClientDatetimeHeader(request);
            String token = validateTokenRequest(request);

            GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), VersionApiEnum.fromValue(versionPath).getValue());
            GlobalContext.setProperty(PropertiesEnum.CLIENT_IP.getValue(), clientIpHeader);
            GlobalContext.setProperty(PropertiesEnum.CLIENT_USER.getValue(), clientUserHeader);
            GlobalContext.setProperty(PropertiesEnum.CLIENT_DATETIME.getValue(), clientDatetime);
            GlobalContext.setProperty(PropertiesEnum.AUTH_TOKEN.getValue(), token);

            chain.doFilter(request, response);

        } catch (ServiceException ex) {
            handleServiceException(ex, request, response);
        }
    }

    private String validateClientIpHeader(HttpServletRequest request) {
        String clientIp = request.getHeader(PropertiesEnum.CLIENT_IP.getValue());
        if (clientIp == null || clientIp.isEmpty()) {
            throw new ServiceException(ErrorCode.HEADER_CLIENT_IP_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            InetAddress ip = InetAddress.getByName(clientIp);
            if (!ip.getHostAddress().equals(clientIp)) {
                throw new ServiceException(ErrorCode.HEADER_CLIENT_IP_INVALID, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.HEADER_CLIENT_IP_INVALID, HttpStatus.BAD_REQUEST);
        }
        return clientIp;
    }

    private String validateClientUserHeader(HttpServletRequest request) {
        String clientUser = request.getHeader(PropertiesEnum.CLIENT_USER.getValue());
        if (clientUser == null || clientUser.isEmpty()) {
            throw new ServiceException(ErrorCode.HEADER_CLIENT_USER_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return clientUser;
    }

    private String validateClientDatetimeHeader(HttpServletRequest request) {
        String clientDatetime = request.getHeader(PropertiesEnum.CLIENT_DATETIME.getValue());
        if (clientDatetime == null || clientDatetime.isEmpty()) {
            throw new ServiceException(ErrorCode.HEADER_CLIENT_DATETIME_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        String datetimePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";
        if (!clientDatetime.matches(datetimePattern)) {
            throw new ServiceException(ErrorCode.HEADER_CLIENT_DATETIME_INVALID, HttpStatus.BAD_REQUEST);
        }
        return clientDatetime;
    }

    private String validateVersionPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (requestURI.startsWith(contextPath)) {
            String pathInfo = requestURI.replace(contextPath.concat("/"), "");
            if (pathInfo.matches("v[1-9]\\d?(/.*)?")) {
                String version = pathInfo.split("/")[0];
                try {
                    return version;
                } catch (IllegalArgumentException e) {
                    throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            }
        }
        throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    private String validateTokenRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        if (token == null) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        return token;
    }

    private void handleServiceException(ServiceException exception, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpCode(String.valueOf(exception.getHttpStatus().value()))
                .httpMethod(request.getMethod())
                .url(request.getRequestURL().toString())
                .errorCode(exception.getErrorCode().getName())
                .errorMessage(exception.getMessage())
                .errorMessageDetail(ObjectUtils.isEmpty(exception.getCause()) ?
                        exception.getMessage() : exception.getCause().getMessage())
                .timestamp(String.valueOf(LocalDateTime.now()))
                .build();
        response.getWriter().write(new Gson().toJson(errorResponse));
        response.setStatus(exception.getHttpStatus().value());

        log.error("Error -> {}", errorResponse);
    }

}
