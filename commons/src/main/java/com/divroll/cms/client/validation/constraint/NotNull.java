package com.divroll.cms.client.validation.constraint;

import com.divroll.cms.client.validation.validators.NotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotNullValidator.class)
@Target( { ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@javax.validation.constraints.NotNull
public @interface NotNull {
    String message() default "Field should not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
