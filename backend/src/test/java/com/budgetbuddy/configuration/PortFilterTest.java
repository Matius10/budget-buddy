package com.budgetbuddy.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

class PortFilterTest {

    private static final String PORT = "8888";
    private static final String PATH_PREFIX = "/pathPrefix";
    private static final String ERROR_MSG = "{\"code\":403,\"message\":\"Forbidden access\"}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final FilterChain MOCK_FILTER_CHAIN = new MockFilterChain();

    private final PortFilter portFilter = new PortFilter(PORT, PATH_PREFIX, OBJECT_MAPPER);

    @Test
    void shouldGoThroughPortFilterWithoutAnyActions() {
        // given
        HttpServletRequest request = prepareRequest(PATH_PREFIX, PORT);
        HttpServletResponse response = prepareResponse();

        // when
        portFilter.doFilter(request, response, MOCK_FILTER_CHAIN);

        // then
        Assertions.assertThat(response.getStatus()).isNotEqualTo(403);
    }

    @ParameterizedTest
    @CsvSource({"/something,8888", "/pathPrefix,8080"})
    void shouldReturnErrorResponseWithForbiddenStatus(String path, String port) throws UnsupportedEncodingException {
        // given
        HttpServletRequest request = prepareRequest(path, port);
        HttpServletResponse response = prepareResponse();

        // when
        portFilter.doFilter(request, response, MOCK_FILTER_CHAIN);

        // then
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
        Assertions.assertThat(((MockHttpServletResponse) response).getContentAsString()).isEqualTo(ERROR_MSG);
    }

    private HttpServletRequest prepareRequest(String requestUri, String requestPort) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(requestUri);
        request.setLocalPort(Integer.parseInt(requestPort));
        return request;
    }

    private HttpServletResponse prepareResponse() {
        return new MockHttpServletResponse();
    }

}