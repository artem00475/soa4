package itmo.tuchin.nikitin.first_service.configuration;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.*;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayloadValidator extends PayloadValidatingInterceptor {
    private final Pattern elementPattern = Pattern.compile("'tns:(.+)'");

    @Override
    protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors) throws TransformerException {
        for(SAXParseException error : errors) {
            this.logger.warn("XML validation error on request: " + error.getMessage());
        }

        if (messageContext.getResponse() instanceof SoapMessage response) {
            SoapBody body = response.getSoapBody();
            SoapFault fault = body.addClientOrSenderFault(this.getFaultStringOrReason(), this.getFaultStringOrReasonLocale());
            if (this.getAddValidationErrorDetail()) {
                SoapFaultDetail detail = fault.addFaultDetail();
                Set<String> invalidParameters = new HashSet<>();
                for(SAXParseException error : errors) {
                    Matcher isElement = elementPattern.matcher(error.getMessage());
                    if (isElement.find()) {
                        invalidParameters.add(isElement.group(1));
                    }
                }
                detail.addFaultDetailElement(new QName("code")).addText("400");
                detail.addFaultDetailElement(new QName("message")).addText("Not valid parameters:" + String.join(",", invalidParameters));
            }
        }

        return false;
    }
}
