/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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

package org.beanplanet.core.mediatypes;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SimpleMediaType implements MediaType {
    /** The full name of the media type, such as text/html. */
    private String name;
    /** A descriptuive name of the media type. */
    private String description;

    private List<String> knownFileExtensions = Collections.emptyList();

    public SimpleMediaType(String name) {
        Assert.notNull(name, "The media type name may not be null");
        this.name = name;
    }


    /**
     * Gets the media type name, such as text/html.
     *
     * @return the full name of the media type.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the media type name, such as text/html.
     *
     * @param name the full media type name, which may not be null.
     */
    public void setName(String name) {
        Assert.notNull(name, "The media type name may not be null");
        this.name = name;
    }

    /**
     * Returns the description of the object.
     *
     * @return the description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the descriptive name of the media type.
     *
     * @param description a description of the media type.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the descriptive name of the media type, returning this instance for method chaining.
     *
     * @param description a description of the media type.
     * @return this instance for method chaining.
     */
    public SimpleMediaType withDescription(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Gets the base of the media type name. For the JPEG image content type, <b>image</b>/jpeg, the base name is
     * <b>image</b>.
     *
     * @return the base name part of the media type name.
     */
    @Override
    public String getBaseType() {
        int slashPos = name.indexOf('/');
        return slashPos >= 0 ? name.substring(0, slashPos) : name;
    }

    /**
     * Gets the subtype name of the media type name. For the JPEG image content type, image/<b>jpeg</b>, the base
     * name is <b>jpeg</b>.
     *
     * @return the subtype name part of the media type name.
     */
    @Override
    public String getSubType() {
        int slashPos = name.indexOf('/');
        return slashPos >= 0 ? name.substring(slashPos+1) : "";
    }

    /**
     * Gets the known file extensions (not including the leading dot) for the content type. For text documents, for
     * example, these might be <b>txt</b> and <b>text</b>.
     *
     * @return the known content types of the media type, in preference order with first preference first, in lowercase.
     */
    @Override
    public List<String> getFileExtensions() {
        return knownFileExtensions;
    }

    /**
     * Sets the known file extensions (not including the leading dot) for the content type. For text documents, for
     * example, these might be <b>txt</b> and <b>text</b>.
     *
     * @param knownFileExtensions the known file extensions for the media type.
     */
    public void setFileExtensions(List<String> knownFileExtensions) {
        this.knownFileExtensions = knownFileExtensions;
    }

    /**
     * Sets the known file extensions (not including the leading dot) for the content type. For text documents, for
     * example, these might be <b>txt</b> and <b>text</b>, returning this instance for method chaining.
     *
     * @param knownFileExtensions the known file extensions for the media type.
     * @return this instance for method chaining.
     */
    public SimpleMediaType withFileExtensions(List<String> knownFileExtensions) {
        setFileExtensions(knownFileExtensions);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SimpleMediaType that = (SimpleMediaType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
