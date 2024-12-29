package com.yo1000.api.application.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.validation.*;

import java.io.IOException;
import java.util.Set;

public class ValidatableObjectReader extends ObjectReader {
    public ValidatableObjectReader(ObjectReader base) {
        super(base, base.getFactory());
    }

    @Override
    protected Object _bindAndClose(JsonParser p0) throws IOException {
        Object bean = super._bindAndClose(p0);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return bean;
    }
}
