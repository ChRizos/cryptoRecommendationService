package cri.sw.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.sql.Timestamp;

@AllArgsConstructor
public class ApiError {

    @Setter
    @Getter
    private Timestamp timestamp;

    @Setter
    @Getter
    private int status;

    @Setter
    @Getter
    private HttpStatusCode error;

    @Setter
    @Getter
    private String message;

    @Setter
    @Getter
    private String path;
}