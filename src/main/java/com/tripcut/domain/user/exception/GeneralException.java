package com.tripcut.domain.user.exception;

import com.tripcut.domain.user.exception.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

//    public ErrorReasonDto getErrorReason() {
//        return this.code.getReason();
//    }

//    public ErrorReasonDto getErrorReasonHttpStatus() {
//        return this.code.getReasonHttpStatus();
//    }
}