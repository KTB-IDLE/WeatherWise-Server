package com.idle.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(HttpStatus.OK, "success", "요청에 성공했습니다."),
    // Not Found Error
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "4040", "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "4042", "존재하지 않는 API 엔드포인트입니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "4043", "해당 파일을 찾을 수 없습니다."),
    NOT_FOUND_MISSION(HttpStatus.NOT_FOUND, "4044" , "해당 미션이 존재하지 않습니다."),
    NOT_FOUND_MISSION_HISTORY(HttpStatus.NOT_FOUND, "4045" , "해당 미션 기록이 존재하지 않습니다."),
    NOT_FOUND_LEVEL(HttpStatus.NOT_FOUND, "4046" , "해당 레벨은 존재하지 않습니다."),

    // Server, File Up/DownLoad Error
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "API 서버 오류입니다."),

    // Access Denied Error
    ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "4030", "액세스 권한이 없습니다."),

    // Bad Request Error
    NOT_END_POINT(HttpStatus.BAD_REQUEST, "4000", "End Point 가 존재하지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4001", "Invalid PARAMETER"),
    BAD_DATA(HttpStatus.BAD_REQUEST, "4003", "데이터가 올바르지 않습니다."),
    BAD_REQUEST_JSON(HttpStatus.BAD_REQUEST, "4004", "잘못된 JSON 형식입니다."),
    INVALID_PARAMETER_FORMAT(HttpStatus.BAD_REQUEST, "4005", "요청에 유효하지 않은 파라미터 형식입니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "4006", "필수 요청 파라미터가 누락되었습니다."),


    /**
     * 401 Unauthorized: Authentication and Authorization Error
     */
    EXPIRED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "4010", "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "4011", "유효하지 않은 토큰입니다."),
    FAILURE_LOGIN(HttpStatus.UNAUTHORIZED, "4012", "로그인에 실패했습니다."),
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "4013", "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(HttpStatus.UNAUTHORIZED, "4014", "토큰 타입이 일치하지 않습니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.UNAUTHORIZED, "4015", "지원하지않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "4016", "알 수 없는 토큰입니다."),

    /**
     * 502 Bad Gateway: Gateway Server Error
     */
    AUTH_SERVER_USER_INFO_ERROR(HttpStatus.BAD_GATEWAY, "5020", "소셜 서버에서 유저 정보를 가져오는데 실패하였습니다.");

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
