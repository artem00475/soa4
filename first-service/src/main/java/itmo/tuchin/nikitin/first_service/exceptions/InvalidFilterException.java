package itmo.tuchin.nikitin.first_service.exceptions;

public class InvalidFilterException extends RuntimeException {
    public InvalidFilterException(String message) {
        super(message);
    }
}
