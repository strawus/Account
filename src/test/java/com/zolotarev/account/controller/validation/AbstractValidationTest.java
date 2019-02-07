package com.zolotarev.account.controller.validation;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Base class for validation testing
 */
public abstract class AbstractValidationTest {

    private Validator validator;
    private ValidatorFactory validatorFactory;

    @BeforeClass
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public void tearDown() {
        validatorFactory.close();
    }

    /**
     * Inheritor must contain {@link org.testng.annotations.DataProvider} with name 'dtos',
     * which must contain dto for testing and expected validation result
     */
    @Test(dataProvider = "dtos")
    public void validationTest(Object dto, boolean isValid) {
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(dto);
        assertEquals(constraintViolations.isEmpty(), isValid);
    }
}
