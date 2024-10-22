package itmo.tuchin.nikitin.first_service.exceptions;

public class InvalidFilterParameterException extends RuntimeException {
    public InvalidFilterParameterException(String message) {
        super(message);
    }
}
