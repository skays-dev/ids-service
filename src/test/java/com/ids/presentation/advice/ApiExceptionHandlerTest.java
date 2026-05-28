package com.ids.presentation.advice;

import com.ids.domain.exception.DomainException;
import com.ids.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void badCredentials_shouldReturnUnauthorizedResponse() {
        var response = handler.badCredentials();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertBody(response.getBody(), 401, "Unauthorized", "Invalid username or password");
    }

    @Test
    void notFound_shouldReturnNotFoundResponse() {
        var response = handler.notFound(new ResourceNotFoundException("Missing resource"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertBody(response.getBody(), 404, "Not Found", "Missing resource");
    }

    @Test
    void badRequest_shouldReturnBadRequestForDomainException() {
        var response = handler.badRequest(new DomainException("Invalid domain state"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertBody(response.getBody(), 400, "Bad Request", "Invalid domain state");
    }

    @SuppressWarnings("unchecked")
    private void assertBody(Map<String, Object> body, int status, String error, String message) {
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("status", status);
        assertThat(body).containsEntry("error", error);
        assertThat(body).containsEntry("message", message);
        assertThat(body).containsKey("timestamp");
    }
}
