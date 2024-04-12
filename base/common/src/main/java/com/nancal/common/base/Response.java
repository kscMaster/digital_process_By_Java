package com.nancal.common.base;


import com.nancal.common.enums.ErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Response<T> extends Message {

    @ApiModelProperty(value = "响应对象")
    private T data;

    @ApiModelProperty(value = "表单验证失败信息")
    private Map<String, String> errors;

    @Builder
    public Response(int code, String msg, T data, Map<String, String> errors) {
        super(code, msg);
        this.data = data;
        this.errors = errors;
    }

    public static <T> Response<T> of(T data) {
        return Response.<T>builder().data(data).build();
    }

    public static <T> Response<T> of() {
        return Response.<T>builder().build();
    }

    public static <T> Response<T> of(T data, String msg) {
        return Response.<T>builder()
                .data(data)
                .msg(msg)
                .build();
    }

    public static Response<Boolean> formBindException(BindException e) {
        ObjectError objectError = e.getAllErrors().stream().findFirst().orElse(null);
        String msg = objectError != null ? objectError.getDefaultMessage() : "";
        String field = objectError != null ? objectError.getObjectName() : "";
        ErrorCode code = field.toLowerCase().contains("id") ? ErrorCode.ID_ERROR : ErrorCode.E_10;
        return Response.<Boolean>builder()
                .code(code.code())
                .msg(msg)
                //.errors(e.getAllErrors().stream().collect(Collectors.toMap(ObjectError::getObjectName, ObjectError::getDefaultMessage)))
                .data(false)
                .build();
    }

    public static Response<Boolean> formConstraintViolationException(ConstraintViolationException e) {
        ConstraintViolation<?> constraintViolation = e.getConstraintViolations().stream().findFirst().orElse(null);
        String msg = constraintViolation != null ? constraintViolation.getMessage() : "";
        String field = constraintViolation != null ? constraintViolation.getPropertyPath().toString() : "";
        ErrorCode code = field.toLowerCase().contains("id") ? ErrorCode.ID_ERROR : ErrorCode.E_10;
        return Response.<Boolean>builder()
                .code(code.code())
                .msg(msg)
                //.errors(e.getConstraintViolations().stream().collect(Collectors.toMap(keyFun, ConstraintViolation::getMessage)))
                .data(false)
                .build();
    }

    public static Response<Object> formMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "";
        String field = fieldError != null ? fieldError.getField() : "";
        ErrorCode code = field.toLowerCase().contains("id") ? ErrorCode.ID_ERROR : ErrorCode.E_10;
        return Response.<Object>builder()
                .code(code.code())
                .msg(msg)
                .errors(e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, ObjectError::getDefaultMessage)))
                .build();
    }
}
