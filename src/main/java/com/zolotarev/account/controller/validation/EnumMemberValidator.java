package com.zolotarev.account.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that the text must be validated as member of declared in {@link EnumMember}
 */
public class EnumMemberValidator implements ConstraintValidator<EnumMember, String> {

    private Enum<?>[] enumConstants;

    @Override
    public void initialize(EnumMember constraint) {
        final Class<? extends Enum<?>> value = constraint.value();
        enumConstants = value.getEnumConstants();
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext context) {
        return isEnumMember(text);
    }

    /**
     * Checks that the text is a member of an enum ignore case
     *
     * @param text Some text to checking
     * @return If an enum contains value, which matches the text - true, or else - false
     */
    private boolean isEnumMember(String text) {
        if (text == null)
            return false;

        for (Enum<?> enumConstant : enumConstants) {
            if (enumConstant.name().equalsIgnoreCase(text.trim())) {
                return true;
            }
        }
        return false;
    }
}