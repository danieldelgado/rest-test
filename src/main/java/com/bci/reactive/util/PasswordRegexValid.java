package com.bci.reactive.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PasswordRegexValidValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordRegexValid {
    String message() default "Invalid password value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}