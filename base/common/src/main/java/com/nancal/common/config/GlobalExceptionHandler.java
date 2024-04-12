package com.nancal.common.config;

import com.nancal.common.base.Response;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.IErrorCode;
import com.nancal.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Response<Boolean> serviceExceptionHandler(HttpServletRequest req, ServiceException e) {
        log.error("[ServiceException]", e);
        log.error("发生业务异常！原因是：{}", e.getMessage());
        IErrorCode code = e.getCode();
        return Response.<Boolean>builder()
                .code(code.code())
                .msg(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Boolean> httpMessageNotReadableExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("[HttpMessageNotReadableException]", e);
        log.error("发生参数异常！原因是：{}", e.getMessage());
        return Response.<Boolean>builder()
                .code(ErrorCode.E_10.code())
                .msg("数据格式输入有误")
                .data(false)
                .build();
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public Response<Boolean> propertyReferenceExceptionExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("[PropertyReferenceException]", e);
        log.error("发生参数异常！原因是：{}", e.getMessage());
        return Response.<Boolean>builder()
                .code(ErrorCode.E_10.code())
                .msg("数据格式输入有误")
                .data(false)
                .build();
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public Response<Boolean> invalidDataAccessApiUsageExceptionHandler(HttpServletRequest req, InvalidDataAccessApiUsageException e) {
        log.error("[InvalidDataAccessApiUsageException]", e);
        log.error("发生查询参数异常！原因是：{}", e.getMessage());
        return Response.<Boolean>builder()
                .code(ErrorCode.ERROR.code())
                .msg("无效的参数")
                .build();
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response<Boolean> constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        log.error("[constraintViolationExceptionHandler]", e);
        return Response.<Boolean>formConstraintViolationException(e);
    }

    @ExceptionHandler(value = BindException.class)
    public Response<Boolean> bindExceptionHandler(HttpServletRequest req, BindException e) {
        log.error("[BindException]", e);
        log.error("验证失败：{}", e.getAllErrors());
        return Response.<Boolean>formBindException(e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response<Object> methodArgumentNotValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);
        log.error("验证失败：{}", e);
        return Response.<Object>formMethodArgumentNotValidException(e);
    }

    @ExceptionHandler(value = Exception.class)
    public Response<Boolean> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("[Exception]", e);
        log.error("发生未知异常！原因是：{}", e.getMessage());
        return Response.<Boolean>builder()
                .code(ErrorCode.ERROR.code())
                .msg("未知异常")
                .build();
    }
}
