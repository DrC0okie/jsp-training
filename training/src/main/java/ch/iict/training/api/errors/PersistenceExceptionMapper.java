package ch.iict.training.api.errors;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
    @Override public Response toResponse(PersistenceException e) {
        var body = ApiError.of("database_error", "Invalid data");
        return Response.status(400).type(MediaType.APPLICATION_JSON).entity(body).build();
    }
}

