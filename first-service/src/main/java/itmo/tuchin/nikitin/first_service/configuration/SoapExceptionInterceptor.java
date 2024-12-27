package itmo.tuchin.nikitin.first_service.configuration;

import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class SoapExceptionInterceptor extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName MESSAGE = new QName("message");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        SoapFaultDetail detail = fault.addFaultDetail();
        HttpStatus code;
        String message;
        if (ex instanceof EntityNotFoundException) {
            code = HttpStatus.NOT_FOUND;
            message = "Person not found";
        } else if (ex instanceof InvalidFilterException) {
            code = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        } else {
            code = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Something went wrong";
        }

        detail.addFaultDetailElement(CODE).addText(String.valueOf(code.value()));
        detail.addFaultDetailElement(MESSAGE).addText(message);
    }
}
