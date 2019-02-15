package com.zolotarev.account.controller.validation;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Base class for validation with groups testing
 */
public abstract class AbstractValidationWithGroupsTest {

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
     * Test calls, if inheritor contains {@link DataProvider} with name 'dtosWithGroups',
     * which provides: dtos for testing, expected validation result and validation groups array
     */
    @Test(dataProvider = "dtosWithGroups")
    public void validationWithGroupsTest(Object dto, boolean isValid, Class<?>... groups) {
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(dto, groups);
        assertEquals(constraintViolations.isEmpty(), isValid);
    }
}
