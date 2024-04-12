package com.nancal.common.exception;


import com.nancal.common.enums.IErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class TipServiceException extends ServiceException {
    private final IErrorCode code;

    public TipServiceException(IErrorCode code) {
        super(code);
        this.code = code;
    }

    public TipServiceException(IErrorCode code, String message) {
        super(code,message);
        this.code = code;
    }

    public TipServiceException(String message, Throwable cause, IErrorCode code) {
        super(message, cause,code);
        this.code = code;
    }

    public TipServiceException(Throwable cause, IErrorCode code) {
        super(cause,code);
        this.code = code;
    }

    public TipServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace,code);
        this.code = code;
    }
}
