package org.beanplanet.validation;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailAddressValidatorTest {
    @Test
    public void simpleFormat() {
        // Given
        EmailAddressValidator<String> validator = EmailAddressValidator.<String>builder()
                                                                       .message(b -> b.code("theCode").parameterisedMessage("The message"))
                                                                       .simpleFormat()
                                                                       .build();

        // Then
        assertThat(validator.validate("@domain.com").hasErrors(), is(true));
        assertThat(validator.validate("@domain.com").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@").hasErrors(), is(true));
        assertThat(validator.validate("username@").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@domain.com").hasErrors(), is(false));
        assertThat(validator.validate("an.other@domain.com").hasErrors(), is(false));
        assertThat(validator.validate("an.other@domain.com").hasErrors(), is(false));
    }

    @Test
    public void rfc5322Format() {
        // Given
        EmailAddressValidator<String> validator = EmailAddressValidator.<String>builder()
                                                                       .message(b -> b.code("theCode").parameterisedMessage("The message"))
                                                                       .rfc5322()
                                                                       .build();

        // Then
        assertThat(validator.validate("@domain.com").hasErrors(), is(true));
        assertThat(validator.validate("@domain.com").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@").hasErrors(), is(true));
        assertThat(validator.validate("username@").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("@world@domain.com").hasErrors(), is(true));
        assertThat(validator.validate("@world@domain.com").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@domain.com").hasErrors(), is(false));
    }

    @Test
    public void gmailPlusAddressing() {
        // Given
        EmailAddressValidator<String> validator = EmailAddressValidator.<String>builder()
                                                                       .message(b -> b.code("theCode").parameterisedMessage("The message"))
                                                                       .gmailPlusAddressing()
                                                                       .build();

        // Then
        assertThat(validator.validate("@domain.com").hasErrors(), is(true));
        assertThat(validator.validate("@domain.com").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@").hasErrors(), is(true));
        assertThat(validator.validate("username@").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@domain.com").hasErrors(), is(false));

        assertThat(validator.validate("username+other@domain.com").hasErrors(), is(false));
    }

    @Test
    public void owasp() {
        // Given
        EmailAddressValidator<String> validator = EmailAddressValidator.<String>builder()
                                                                       .message(b -> b.code("theCode").parameterisedMessage("The message"))
                                                                       .owasp()
                                                                       .build();

        // Then
        assertThat(validator.validate("@domain.com").hasErrors(), is(true));
        assertThat(validator.validate("@domain.com").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@").hasErrors(), is(true));
        assertThat(validator.validate("username@").hasErrorWithCode("theCode"), is(true));

        assertThat(validator.validate("username@domain.com").hasErrors(), is(false));
        assertThat(validator.validate("an.other@domain.com").hasErrors(), is(false));
    }
}
