/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.messages.domain;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class MessagesImplTest {
    @Test
    public void messagesFactoryMethod() {
        assertThat(messages().getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages().getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages().getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void ctorNoArgs() {
        assertThat(new MessagesImpl().getInfos(), equalTo(Collections.emptyList()));
        assertThat(new MessagesImpl().getWarnings(), equalTo(Collections.emptyList()));
        assertThat(new MessagesImpl().getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void ctorWithInfosWarningsAndErrors() {
        // Given
        List<Message> infos = Collections.emptyList();
        List<Message> warnings = Collections.emptyList();
        List<Message> errros = Collections.emptyList();

        // When
        MessagesImpl messages = new MessagesImpl(infos, warnings, errros);

        // Then
        assertThat(messages.getInfos(), equalTo(infos));
        assertThat(messages.getWarnings(), equalTo(warnings));
        assertThat(messages.getErrors(), equalTo(errros));
    }

    @Test
    public void addInfo() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        MessagesImpl messages = messages();

        // When
        messages.addInfo(message);

        // Then
        assertThat(messages.isEmpty(), is(false));
        assertThat(messages.getInfos().size(), equalTo(1));
        assertThat(messages.getInfos().get(0), equalTo(message));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addInfoCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addInfo("theCode", "theMessage");

        // Then
        assertThat(messages.getInfos().size(), equalTo(1));
        assertThat(messages.getInfos().get(0).getField(), nullValue());
        assertThat(messages.getInfos().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getInfos().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getInfos().get(0).getMessageParameters(), nullValue());
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addInfoCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addInfo("theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos().size(), equalTo(1));
        assertThat(messages.getInfos().get(0).getField(), nullValue());
        assertThat(messages.getInfos().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getInfos().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getInfos().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getInfos().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getInfos().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getInfos().get(0).getMessageParameters()[1], sameInstance(param2));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addFieldInfoFieldCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addFieldInfo("theField", "theCode", "theMessage");

        // Then
        assertThat(messages.getInfos().size(), equalTo(1));
        assertThat(messages.getInfos().get(0).getField(), equalTo("theField"));
        assertThat(messages.getInfos().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getInfos().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getInfos().get(0).getMessageParameters(), nullValue());
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addFieldInfoCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addFieldInfo("theField", "theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos().size(), equalTo(1));
        assertThat(messages.getInfos().get(0).getField(), equalTo("theField"));
        assertThat(messages.getInfos().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getInfos().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getInfos().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getInfos().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getInfos().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getInfos().get(0).getMessageParameters()[1], sameInstance(param2));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addWarning() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        MessagesImpl messages = messages();

        // When
        messages.addWarning(message);

        // Then
        assertThat(messages.isEmpty(), is(false));
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings().size(), equalTo(1));
        assertThat(messages.getWarnings().get(0), equalTo(message));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addWarningCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addWarning("theCode", "theMessage");

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings().size(), equalTo(1));
        assertThat(messages.getWarnings().get(0).getField(), nullValue());
        assertThat(messages.getWarnings().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getWarnings().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getWarnings().get(0).getMessageParameters(), nullValue());
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addWarningCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addWarning("theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings().size(), equalTo(1));
        assertThat(messages.getWarnings().get(0).getField(), nullValue());
        assertThat(messages.getWarnings().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getWarnings().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getWarnings().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getWarnings().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getWarnings().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getWarnings().get(0).getMessageParameters()[1], sameInstance(param2));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addWarningInfoFieldCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addFieldWarning("theField", "theCode", "theMessage");

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings().size(), equalTo(1));
        assertThat(messages.getWarnings().get(0).getField(), equalTo("theField"));
        assertThat(messages.getWarnings().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getWarnings().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getWarnings().get(0).getMessageParameters(), nullValue());
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addFieldWarningCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addFieldWarning("theField", "theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings().size(), equalTo(1));
        assertThat(messages.getWarnings().get(0).getField(), equalTo("theField"));
        assertThat(messages.getWarnings().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getWarnings().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getWarnings().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getWarnings().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getWarnings().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getWarnings().get(0).getMessageParameters()[1], sameInstance(param2));
        assertThat(messages.getErrors(), equalTo(Collections.emptyList()));
    }

    @Test
    public void addError() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        MessagesImpl messages = messages();

        // When
        messages.addError(message);

        // Then
        assertThat(messages.isEmpty(), is(false));
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors().size(), equalTo(1));
        assertThat(messages.getErrors().get(0), equalTo(message));
    }

    @Test
    public void addErrorCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addError("theCode", "theMessage");

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors().size(), equalTo(1));
        assertThat(messages.getErrors().get(0).getField(), nullValue());
        assertThat(messages.getErrors().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getErrors().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getErrors().get(0).getMessageParameters(), nullValue());
    }

    @Test
    public void addErrorCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addError("theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors().size(), equalTo(1));
        assertThat(messages.getErrors().get(0).getField(), nullValue());
        assertThat(messages.getErrors().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getErrors().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getErrors().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getErrors().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getErrors().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getErrors().get(0).getMessageParameters()[1], sameInstance(param2));
    }

    @Test
    public void addFieldErrorFieldCodeAndMessage() {
        // Given
        MessagesImpl messages = messages();

        // When
        messages.addFieldError("theField", "theCode", "theMessage");

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors().size(), equalTo(1));
        assertThat(messages.getErrors().get(0).getField(), equalTo("theField"));
        assertThat(messages.getErrors().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getErrors().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getErrors().get(0).getMessageParameters(), nullValue());
    }

    @Test
    public void addFieldErrorCodeMessageAndParameters() {
        // Given
        Object param1 = new Object();
        Object param2 = new Object();
        MessagesImpl messages = messages();

        // When
        messages.addFieldError("theField", "theCode", "theMessage", param1, param2);

        // Then
        assertThat(messages.getInfos(), equalTo(Collections.emptyList()));
        assertThat(messages.getWarnings(), equalTo(Collections.emptyList()));
        assertThat(messages.getErrors().size(), equalTo(1));
        assertThat(messages.getErrors().get(0).getField(), equalTo("theField"));
        assertThat(messages.getErrors().get(0).getCode(), equalTo("theCode"));
        assertThat(messages.getErrors().get(0).getParameterisedMessage(), equalTo("theMessage"));
        assertThat(messages.getErrors().get(0).getMessageParameters(), notNullValue());
        assertThat(messages.getErrors().get(0).getMessageParameters().length, equalTo(2));
        assertThat(messages.getErrors().get(0).getMessageParameters()[0], sameInstance(param1));
        assertThat(messages.getErrors().get(0).getMessageParameters()[1], sameInstance(param2));
    }

    @Test
    public void size() {
        assertThat(messages().size(), is(0));

        assertThat(messages().addInfo("theCode", "theMessage").size(), is(1));
        assertThat(messages().addWarning("theCode", "theMessage").size(), is(1));
        assertThat(messages().addError("theCode", "theMessage").size(), is(1));

        assertThat(messages()
                           .addInfo("theCode", "theMessage")
                           .addWarning("theCode", "theMessage")
                           .addError("theCode", "theMessage")
                           .size(), is(3));
    }

    @Test
    public void hasInfos() {
        assertThat(messages().hasInfos(), is(false));
        assertThat(messages().addInfo("theCode", "theMessage").hasInfos(), is(true));
    }

    @Test
    public void hasWarnings() {
        assertThat(messages().hasWarnings(), is(false));
        assertThat(messages().addWarning("theCode", "theMessage").hasWarnings(), is(true));
    }

    @Test
    public void hasErrors() {
        assertThat(messages().hasErrors(), is(false));
        assertThat(messages().addError("theCode", "theMessage").hasErrors(), is(true));
    }


    @Test
    public void equalsWithSameInstance() {
        // Given
        MessagesImpl messages = messages();

        // Then
        assertThat(messages.equals(messages), is(true));
    }

    @Test
    public void equalsWithAnotherType() {
        // Given
        MessagesImpl messages = messages();

        // Then
        assertThat(messages.equals(new Object()), is(false));
    }

    @Test
    public void equalsWithNull() {
        // Given
        MessagesImpl messages = messages();

        // Then
        assertThat(messages.equals(null), is(false));
    }

    @Test
    public void equalsOther() {
        // Given
        MessagesImpl messages1 = messages()
                .addInfo("theCode", "theMessage")
                .addWarning("theCode", "theMessage")
                .addError("theCode", "theMessage");
        MessagesImpl messages2 = messages()
                .addInfo("theCode", "theMessage")
                .addWarning("theCode", "theMessage")
                .addError("theCode", "theMessage");

        // Then
        assertThat(messages1, equalTo(messages2));
    }

    @Test
    public void hashCodeTest() {
        // Given
        MessagesImpl messages = messages()
                .addInfo("theCode", "theMessage")
                .addWarning("theCode", "theMessage")
                .addError("theCode", "theMessage");

        // Then
        assertThat(messages.hashCode(), equalTo(Objects.hash(messages.getInfos(), messages.getWarnings(), messages.getErrors())));
    }

    @Test
    public void toStringTest() {
        // Given
        MessagesImpl messages = messages();

        // Then
        assertThat(messages.toString(), containsString("infos="));
        assertThat(messages.toString(), containsString("warnings="));
        assertThat(messages.toString(), containsString("errors="));
    }

    @Test
    public void hasErrorWithCode() {
        // Given
        MessagesImpl messages = messages()
                .addError("theCode", "theMessage");

        // Then
        assertThat(messages.hasErrorWithCode("theCode"), is(true));
        assertThat(messages.hasErrorWithCode("notPresent"), is(false));
    }

}