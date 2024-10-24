package exception;

import dto.ExceptionDTO;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {
    @Override
    public Response toResponse(ProcessingException e) {
        return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ExceptionDTO("Other service unreachable"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
