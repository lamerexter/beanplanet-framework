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

package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.InputStream;

public interface RandomAccessor extends Closeable, DataInput, DataOutput {
    /**
     * Closes this random accessor, freeing any related system resources.
     *
     * @throws IoException if an error occurs closing the accessor
     */
    void close();

    /**
     * Sets the file-pointer offset, measured from the beginning of this resource, at which the next read or write
     * occurs. The offset may be set beyond the end of the resource content. Setting the offset beyond the end of the
     * resource may or may not change the resource length. The resource length will certainly change by writing after the
     * offset has been set beyond the end of the resource.
     *
     * @param pos the offset position, measured in bytes from the beginning of the resource, at which to set the file
     *        pointer.
     * @throws IoException if pos is less than 0 or if an I/O error occurs.
     */
    void seek(long pos);

    /**
     * Reads up to <code>b.length</code> bytes of data from this file into an array of bytes. This method blocks until at
     * least one byte of input is available.
     *
     * <p>
     * Although <code>RandomAccessor</code> is not a subclass of InputStream, this method behaves in exactly the same way
     * as the <code>{@link InputStream#read(byte[])}</code> method of <code>InputStream</code>.
     * <p>
     *
     * @param <code>b</code> the buffer into which the data is read
     * @return the total number of bytes read into the buffer, or <code>-1</code> is there is no more data because the
     *         end of the stream has been reached.
     */
    int read(byte[] b);

    /**
     * Reads up to <code>len</code> bytes of data from this file into an array of bytes. This method blocks until at
     * least one byte of input is available.
     *
     * <p>
     * Although <code>RandomAccessor</code> is not a subclass of InputStream, this method behaves in exactly the same way
     * as the <code>{@link InputStream#read(byte[], int, int)}</code> method of <code>InputStream</code>.
     * <p>
     *
     * @param <code>b</code> the buffer into which the data is read.
     * @param <code>off</code> the start offset of the data.
     * @param <code>len</code> the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
     *         end of the file has been reached.
     */
    int read(byte[] b, int off, int len);
}
