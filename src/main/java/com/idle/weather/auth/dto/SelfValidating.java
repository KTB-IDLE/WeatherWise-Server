package com.idle.weather.auth.dto;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public abstract class SelfValidating<T> {
    private final Validator validator;


    public SelfValidating() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new BaseException(ErrorCode.BAD_DATA);
        }
    }
}

