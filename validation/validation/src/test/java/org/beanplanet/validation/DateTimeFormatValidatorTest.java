package org.beanplanet.validation;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.beanplanet.messages.domain.MessagesImpl;
import org.junit.Test;

public class DateTimeFormatValidatorTest {
  @Test
  public void validatesDateRime_Successfully() {
    assertThat(new DateTimeFormatValidator<String>(
      globalMessage("theCode", "theMessage"),
      "yyyy-MM-dd' 'HH:mm:ss").validate("2021-10-22 18:30:25", MessagesImpl.messages()).hasErrors(), is(false));
  }

  @Test
  public void doesNotValidateDateRime_WhenValueDoesNotMatchFormat() {
    assertThat(new DateTimeFormatValidator<String>(
      globalMessage("theCode", "theMessage"),
      "yyyy-MM-dd HH:mm:ss").validate("2021-10-2218:30:25", MessagesImpl.messages()).hasErrorWithCode("theCode"), is(true));
  }
}