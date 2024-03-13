package com.shop.online.demo.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static com.shop.online.demo.config.ApplicationConfig.DATE_TIME_FORMAT;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ApiSubError> subErrors;

    public ApiError(HttpStatus status, String message) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, List<ApiSubError> subErrors) {
        this(status, message);
        this.subErrors = subErrors;
    }
}

