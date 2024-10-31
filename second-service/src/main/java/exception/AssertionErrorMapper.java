package exception;

import dto.ExceptionDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AssertionErrorMapper implements ExceptionMapper<AssertionError> {
    @Override
    public Response toResponse(AssertionError exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionDTO("Not valid parameters: " + exception.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
