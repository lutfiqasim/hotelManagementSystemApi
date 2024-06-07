package bzu.edu.hotelManagmentAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    
    @JsonProperty("field_errors")
    private Map<String,String> fieldErrors;

    @Override
    public String toString() {
        return "Validation Error{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", fieldErrors=" + fieldErrors +
                '}';
    }
}
