package com.divroll.cms.client.validation.validators;

import com.divroll.cms.client.validation.constraint.NotNull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {

    public void initialize(NotNull constraintAnnotation) {
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        return object != null;
    }

}