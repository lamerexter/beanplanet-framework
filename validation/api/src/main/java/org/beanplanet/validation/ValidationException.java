package org.beanplanet.validation;

import org.beanplanet.core.UncheckedException;
import org.beanplanet.messages.domain.Messages;

/**
 * An exception which may be thrown when validation errors occur.
 */
public class ValidationException extends UncheckedException {
  private Messages messages;

  public ValidationException(Messages messages) {
    this.messages = messages;
  }

  public Messages getMessages() {
    return messages;
  }

  public void setMessages(Messages messages) {
    this.messages = messages;
  }
}
