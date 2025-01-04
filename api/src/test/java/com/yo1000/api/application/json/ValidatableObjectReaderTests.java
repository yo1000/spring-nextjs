package com.yo1000.api.application.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ValidatableObjectReaderTests {
    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            { "name": "Squall", "level": 45, "hp": 6000 }
            { "name": "Rinoa" , "level": 20, "hp": 1500 }
            { "name": "Zell"  , "level": 30, "hp": 0    }
            """)
    @DisplayName("Given ValidatableObjectReader, When invoke readValue with valid value only contained JSON, Then not throw ConstraintViolationException")
    void test_readValue_valid(String patchJson) {
        // Given
        Status status = new Status("valid", 10, 500);
        ObjectReader reader = new ValidatableObjectReader(new ObjectMapper().readerForUpdating(status));

        // When
        // Then
        try {
            reader.readValue(patchJson);
        } catch (Throwable t) {
            Assertions.fail("Not reachable");
        }
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            { "name": ""         , "level": 15 , "hp": 1000  } | name  | [A-Za-z.,:?!_\\-()0-9]{1,9}
            { "name": "*Invalid*", "level": 15 , "hp": 1000  } | name  | [A-Za-z.,:?!_\\-()0-9]{1,9}
            { "name": "Valid"    , "level": 0  , "hp": 1000  } | level | 1
            { "name": "Valid"    , "level": 200, "hp": 1000  } | level | 100
            { "name": "Valid"    , "level": 15 , "hp": -10   } | hp    | 0
            { "name": "Valid"    , "level": 15 , "hp": 20000 } | hp    | 9999
            """)
    @DisplayName("Given ValidatableObjectReader, When invoke readValue with invalid value contained JSON, Then throw ConstraintViolationException")
    void test_readValue_invalid(String patchJson, String field, String messagePart) {
        // Given
        Status status = new Status("valid", 10, 500);
        ObjectReader reader = new ValidatableObjectReader(new ObjectMapper().readerForUpdating(status));

        // When
        ThrowableAssert.ThrowingCallable callable = () -> reader.readValue(patchJson);

        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field, messagePart);
    }

    public static class Status {
        @Pattern(regexp = "[A-Za-z.,:?!_\\-()0-9]{1,9}")
        private String name;
        @Min(1)
        @Max(100)
        private Integer level;
        @Min(0)
        @Max(9_999)
        private Integer hp;

        public Status(String name, Integer level, Integer hp) {
            this.name = name;
            this.level = level;
            this.hp = hp;
        }

        public String getName() {
            return name;
        }

        public Integer getLevel() {
            return level;
        }

        public Integer getHp() {
            return hp;
        }
    }
}
