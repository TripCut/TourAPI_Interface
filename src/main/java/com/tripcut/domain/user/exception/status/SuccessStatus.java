package com.tripcut.domain.user.exception.status;

import com.tripcut.domain.user.dto.ReasonDto;
import com.tripcut.domain.user.exception.code.BaseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    LOGIN_SUCCESS(HttpStatus.OK, "200", "요청 성공"),
    SUCCESS(HttpStatus.OK, "200", "요청 성공"),

    _CREATED(HttpStatus.CREATED, "201", "요청 성공 및 리소스 생성됨");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

//    @Override
//    public ReasonDto getReason() {
//        return ReasonDto.builder().message(message).code(code).isSuccess(true).build();
//    }

//    @Override
//    public ReasonDto getReasonHttpStatus() {
//        return ReasonDto.builder()
//                .message(message)
//                .code(code)
//                .isSuccess(true)
//                .httpStatus(httpStatus)
//                .build();
//    }
}
