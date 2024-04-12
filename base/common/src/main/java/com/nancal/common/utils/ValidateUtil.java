package com.nancal.common.utils;

import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate validator的校验工具
 */
public class ValidateUtil {
    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        validate(constraintViolations);
    }

    /**
     * 通过组来校验实体类
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);
        validate(constraintViolations);
    }

    private static <T> void validate(Set<ConstraintViolation<T>> constraintViolations){
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                System.out.println();
                throw new ServiceException(ErrorCode.E_10,constraintViolation.getMessage());
            }
        }
    }
}