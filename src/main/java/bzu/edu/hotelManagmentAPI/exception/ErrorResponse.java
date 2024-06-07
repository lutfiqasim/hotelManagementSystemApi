package bzu.edu.hotelManagmentAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("timestamp")
    private LocalDateTime timeStamp;

    private String message;
    private String details;


}
