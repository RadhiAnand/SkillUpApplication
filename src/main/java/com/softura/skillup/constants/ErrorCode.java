package com.softura.skillup.constants;

public enum ErrorCode {
    RECORDNOTFOUNDEXCEPTION(1),
    DB_EXCEPTION(2),
    METHODARGUMENTNOTVALIDEXCEPTION(3),EXCEPTION(5),RUNTIMEEXCEPTION(6);

    public final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
