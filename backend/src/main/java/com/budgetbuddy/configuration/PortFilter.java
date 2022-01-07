package com.budgetbuddy.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
class PortFilter implements Filter {

    private final ObjectMapper objectMapper;
    private final Integer trustedPort;
    private final String trustedPathPrefix;

    PortFilter(String trustedPort, String trustedPathPrefix, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.trustedPort = Strings.isNullOrEmpty(trustedPort) ? 0 : Integer.parseInt(trustedPort);
        this.trustedPathPrefix = trustedPathPrefix;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        if (trustedPort > 0) {

            if (isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() != trustedPort) {
                log.warn("Attempt to access trusted endpoint [{}] on untrusted port [{}]", getRequestURI(servletRequest), servletRequest.getLocalPort());
                prepareErrorResponse(servletResponse);
                return;
            }

            if (!isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() == trustedPort) {
                log.warn("Attempt to access untrusted endpoint [{}] on trusted port [{}]", getRequestURI(servletRequest), servletRequest.getLocalPort());
                prepareErrorResponse(servletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isRequestForTrustedEndpoint(ServletRequest servletRequest) {
        return getRequestURI(servletRequest).startsWith(trustedPathPrefix);
    }

    private String getRequestURI(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getRequestURI();
    }

    @SneakyThrows
    private void prepareErrorResponse(ServletResponse response) {
        ((HttpServletResponse) response).setStatus(403);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(PortFilterResponse.create()));
        response.getOutputStream().close();
    }

    @Getter
    private static final class PortFilterResponse {
        private final Integer code = 403;
        private final String message = "Forbidden access";

        static PortFilterResponse create() {
            return new PortFilterResponse();
        }
    }

}
