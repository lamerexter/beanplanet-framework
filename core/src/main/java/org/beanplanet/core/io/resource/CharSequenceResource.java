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
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static org.beanplanet.core.lang.TypeUtil.getBaseName;

/**
 * Implementation of a character sequence resource: a resource with a backing <code>{@link CharSequence}<code>.
 *
 * @author Gary Watson
 */
public class CharSequenceResource extends AbstractResource implements ReadableResource, Comparable<CharSequenceResource> {
    protected CharSequence characterSequence;

    /**
     * Constructs a new character sequence resource with no initial backing string.
     *
     */
    public CharSequenceResource() {
    }

    /**
     * Constructs a new character sequence with the specified backing string.
     *
     * @param characterSequence the character sequence string backing this resource.
     */
    public CharSequenceResource(CharSequence characterSequence) {
        setCharacterSequence(characterSequence);
    }

    /**
     * Returns the character sequence string backing this string resource.
     *
     * @return the characterSequence
     */
    public CharSequence getCharacterSequence() {
        return characterSequence;
    }

    /**
     * Sets the character sequence string backing this resource.
     *
     * @param characterSequence the characterSequence to set
     */
    public void setCharacterSequence(CharSequence characterSequence) {
        this.characterSequence = characterSequence;
    }

    /**
     * Returns the String backing this string resource.
     *
     * @return the string backing this resource.
     */
    public String getString() {
        return getCharacterSequence().toString();
    }

    /**
     * Sets the String backing this string resource.
     *
     * @param string the string to back this resource.
     */
    public void setString(String string) {
        setCharacterSequence(string);
    }

    /**
     * Gets the length of the character sequence backing this resource.
     *
     * @return the length of the character sequence.
     */
    @Override
    public long getContentLength() {
        return characterSequence == null ? -1 : characterSequence.length();
    }

    /**
     * A string resource always exists.
     *
     * @return always true.
     */
    public boolean exists() {
        return true;
    }

    /**
     * Attempts to return a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return the URL of the resource.
     * @throws UnsupportedOperationException if the URL of the resource could not be determined or the type of the
     *         resource is such that URL references are not supported.
     */
    public URL getUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("URL references to string resources is not supported");
    }

    /**
     * Creates a new input stream, suitable for reading the resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    @Override
    public InputStream getInputStream() throws IoException {
        checkInitialised();
        return new ByteArrayInputStream(getString().getBytes());
    }

    private void checkInitialised() {
        Assert.notNull(getCharacterSequence(), "The character sequence string backing this resource may not be null");
    }

    /**
     * Provides a convenient string representation of this resource.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getBaseName(getClass())).append("[string=").append(getCharacterSequence() == null ? null : StringUtil.truncate(getString(), 50, "..."))
            .append("]");
        return buf.toString();
    }


    /**
     * Compares this resource to another.
     *
     * @param other the resource to be compared with this instance.
     * @return -1, 0 or 1 if this resource is less than, equal or greater than the specified resource.
     */
    public int compareTo(CharSequenceResource other) {
        CharSequence otherSequence = (other == null ? null : other.getCharacterSequence());
        if (getCharacterSequence() == null && otherSequence == null) {
            return 0;
        } else if (getCharacterSequence() == null) {
            return -1;
        } else if (otherSequence == null) {
            return 1;
        }

        return getCharacterSequence().toString().compareTo(otherSequence.toString());
    }

    /**
     * Determines whether this resource is equal to another.
     *
     * @return true if this resource is equivalent to the specified resource, false otherwise.
     */
    public boolean equals(Object other) {
        if ( !(other instanceof CharSequenceResource) ) {
            return false;
        }

        return compareTo((CharSequenceResource)other) == 0;
    }

    /**
     * Computes a hash code of this resource.
     *
     * @return the hash code of this resource, based on the resource URL.
     */
    public int hashCode() {
        return Objects.hash(getCharacterSequence());
    }
}

