package com.nancal.remote.config;

import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/12/6 16:46
 * @Description feign远程调用错误信息处理
 */
@Slf4j
@Configuration
public class ExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        Exception exception = null;
        try {
            if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                exception = new ServiceException(ErrorCode.UNAUTHORIZED);
            } else if (response.status() == HttpStatus.FORBIDDEN.value()) {
                exception = new ServiceException(ErrorCode.FORBIDDEN);
            } else {
                String msg = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                exception = new ServiceException(ErrorCode.REMOTE_FAIL, msg);
            }
        } catch (IOException ie) {
            log.error("远程调用异常", ie);
        }
        return exception;
    }
}
