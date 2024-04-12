package com.nancal.common.exception;


import com.nancal.common.enums.IErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class ServiceException extends RuntimeException {
    private final IErrorCode code;

    public ServiceException(IErrorCode code) {
        super(code.msg());
        this.code = code;
    }

    public ServiceException(IErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, IErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, IErrorCode code) {
        super(cause);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
