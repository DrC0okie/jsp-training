package ch.iict.training.api.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ApiError.FieldError> fields = e.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldError(pathOf(v), v.getMessage()))
                .toList();
        ApiError body = new ApiError("validation_error", "Invalid input", fields);
        return Response.status(422).type(MediaType.APPLICATION_JSON).entity(body).build();
    }

    private String pathOf(ConstraintViolation<?> v) {
        return v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
    }
}
