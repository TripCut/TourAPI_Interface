package com.tripcut.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
//@Constraint(validatedBy = TrafficConditionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTrafficCondition {
    String message() default "혼잡도가 높아 방문이 제한됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}