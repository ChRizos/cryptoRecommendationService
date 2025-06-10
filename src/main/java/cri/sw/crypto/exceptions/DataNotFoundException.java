package cri.sw.crypto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class DataNotFoundException extends ResponseStatusException {
    public DataNotFoundException(HttpStatusCode httpStatusCode, String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
