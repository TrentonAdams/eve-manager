package com.github.trentonadams.beanvalidation;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ApiKeyTest
{

    private static Validator validator;

    @BeforeClass
    public static void setUp()
    {
        final ValidatorFactory factory =
            Validation.buildDefaultValidatorFactory();
        //Validation.byProvider(HibernateValidator.class);
        validator = factory.getValidator();
    }

    @Test
    public void invalidKeyId()
    {
        final ApiKey apiKey = new ApiKey();
        apiKey.setKeyId("kskdo");
        apiKey.setVerificationCode("lkajsdodijoixcjlkasf09uw0r9in");

        final Set<ConstraintViolation<ApiKey>> constraintViolations =
            validator.validate(apiKey);

        assertEquals(1, constraintViolations.size());
//        System.out.println(constraintViolations.iterator().next().getMessage());
        assertEquals(
            "Key ID must be an integer.", // from ValidationMessages.properties
            constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void nullKeyId()
    {
        final ApiKey apiKey = new ApiKey();
        apiKey.setVerificationCode("lkajsdodijoixcjlkasf09uw0r9in");

        final Set<ConstraintViolation<ApiKey>> constraintViolations =
            validator.validate(apiKey);

        assertEquals(1, constraintViolations.size());
        //System.out.println(constraintViolations.iterator().next().getMessage());
        assertEquals(
            "Key ID must be an integer.", // from ValidationMessages.properties
            constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void nullVerificationCode()
    {
        final ApiKey apiKey = new ApiKey();
        apiKey.setKeyId("909239");

        final Set<ConstraintViolation<ApiKey>> constraintViolations =
            validator.validate(apiKey);

        assertEquals(1, constraintViolations.size());
        assertEquals(
            "Verification code must not be null.", // from ValidationMessages.properties
            constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void validApiKey()
    {
        final ApiKey apiKey = new ApiKey();
        apiKey.setKeyId("909239");
        apiKey.setVerificationCode("lkajsdf09jasoidfjlaksdjflkasjdhflk");

        final Set<ConstraintViolation<ApiKey>> constraintViolations =
            validator.validate(apiKey);

        assertEquals(0, constraintViolations.size());
    }
}