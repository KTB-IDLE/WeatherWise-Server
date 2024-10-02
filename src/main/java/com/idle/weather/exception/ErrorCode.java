package com.idle.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(HttpStatus.OK , "success" , "요청에 성공했습니다."),

    // Access Denied Error
    ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "4030", "액세스 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    public static ErrorCode findByMessage(String message) {
        for (ErrorCode response : ErrorCode.values()) {
            if (message.equals(response.message)) {
                return response;
            }
        }
        return null;
    }
}
