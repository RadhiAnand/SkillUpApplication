package com.softura.skillup.constants;

public enum ErrorCode {
    RECORDNOTFOUNDEXCEPTION(1),
    DB_EXCEPTION(2),
    METHODARGUMENTNOTVALIDEXCEPTION(3), EXCEPTION(4),RUNTIMEEXCEPTION(5);

    public final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
