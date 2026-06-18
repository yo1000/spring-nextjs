package com.yo1000.api.application.json;

import jakarta.validation.*;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.deser.DeserializationContextExt;

import java.util.Set;

public class ValidatableObjectReader extends ObjectReader {
    public ValidatableObjectReader(ObjectReader base) {
        super(base, base.getConfig());
    }

    @Override
    protected Object _bindAndClose(DeserializationContextExt ctxt, JsonParser p0) throws JacksonException {
        Object bean = super._bindAndClose(ctxt, p0);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return bean;
    }
}
