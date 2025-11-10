/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
