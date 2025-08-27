package com.tripcut.domain.user.exception.handler;

import com.tripcut.domain.user.exception.GeneralException;
import com.tripcut.domain.user.exception.code.BaseErrorCode;

public class AuthHandler extends GeneralException {

    public AuthHandler(BaseErrorCode code) {
        super(code);
    }
}
