package org.beanplanet.validation;

import org.beanplanet.testing.beans.TestBean;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NestedValueValidatorTest {
    @Test
    public void givenANestedProperty_whenValidated_thenTheValidationResult() {
        // Given
        TestBean beanWithValidProperty = new TestBean("stringProperty");
        TestBean beanWithInvalidProperty = new TestBean("");
        NestedValueValidator<TestBean, String> validator = new NestedValueValidator<>(TestBean::getStringProperty,
                new NotEmptyValidator<>());

        // Then
        assertThat(validator.validate(beanWithValidProperty).hasErrors(), is(false));
        assertThat(validator.validate(beanWithInvalidProperty).hasErrors(), is(true));
    }
}