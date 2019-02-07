package com.zolotarev.account.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be a member of an declared enum
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumMemberValidator.class)
public @interface EnumMember {
    Class<? extends Enum<?>> value();
    String message() default "{com.zolotarev.account.controller.validation.EnumMember.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
