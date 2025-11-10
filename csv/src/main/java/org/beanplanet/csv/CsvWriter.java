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

package org.beanplanet.csv;

import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.Resource;

import java.io.Closeable;
import java.io.Writer;
import java.util.List;

import static org.beanplanet.core.io.IoUtil.flushWithRuntimeError;
import static org.beanplanet.core.io.IoUtil.write;

/**
 * The Class CsvWriter.
 */
public class CsvWriter implements Closeable {
    /** The destination where the data is written. */
    protected Writer writerDelegate;

    private StringBuilder tempBuilder = new StringBuilder();

    /**
     * Instantiates a new cSV writer.
     */
    public CsvWriter() {
    }

    /**
     * Instantiates a new CSV writer.
     *
     * @param delegateWriter
     *           the delegate writer
     */
    public CsvWriter(Writer delegateWriter) {
        setWriterDelegate(delegateWriter);
    }

    /**
     * Instantiates a new CSV writer. The content will be written using the platform's default character set.
     *
     * @param resource the delegate resource where the content will be written.
     */
    public CsvWriter(Resource resource) {
        setWriterDelegate(resource.getWriter());
    }

    /**
     * Instantiates a new CSV writer.
     *
     * @param resource the delegate resource where the content will be written.
     * @param charSetName the name of the character set applied to the written content.
     */
    public CsvWriter(Resource resource, String charSetName) {
        setWriterDelegate(resource.getWriter(charSetName));
    }

    /**
     * Gets the destination where the data is written.
     *
     * @return the destination where the data is written
     */
    public Writer getWriterDelegate() {
        return writerDelegate;
    }

    /**
     * Sets the destination where the data is written.
     *
     * @param writerDelegate
     *           the new destination where the data is written
     */
    public void setWriterDelegate(Writer writerDelegate) {
        this.writerDelegate = writerDelegate;
    }

    public void writeRecord(List<?> fields) {
        if (fields == null) {
            return;
        }

        int n=0;
        for (Object field : fields) {
            if (n++ > 0) {
                writeFieldDelimter();
            }
            writeField(field);
        }

        writeRecordDelimter();
    }

    protected void writeField(Object field) {
        if (field == null) {
            return;
        }

        tempBuilder.setLength(0);
        tempBuilder.append(field.toString());
        boolean containsQuotableCharacters = false;
        for (int n=0; n < tempBuilder.length(); n++) {
            char ch = tempBuilder.charAt(n);
            containsQuotableCharacters = containsQuotableCharacters || isQuotableCharacter(ch);
            if (ch == '\"') {
                tempBuilder.insert(n, "\"");
                n++;
            }
        }

        if ( containsQuotableCharacters ) {
            tempBuilder.insert(0, '\"').append('\"');
        }

        write(getWriterDelegate(), tempBuilder);
    }

    protected boolean isQuotableCharacter(char ch) {
        return ",\n\"".indexOf(ch) >= 0;
    }

    protected void writeFieldDelimter() {
        write(getWriterDelegate(), ",");
    }

    protected void writeRecordDelimter() {
        write(getWriterDelegate(), IoUtil.CRLF);
    }

    public void flush() {
        flushWithRuntimeError(getWriterDelegate());
    }

    public void close() {
        IoUtil.close(getWriterDelegate());
    }
}
