package com.company.scaffold.common.core.exception;

import com.company.scaffold.common.core.result.ResultCodeInterface;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCodeInterface resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BusinessException(ResultCodeInterface resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }
}
