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

package org.beanplanet.core.net.http;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.models.Named;
import org.beanplanet.core.models.Pair;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.core.util.StringUtil;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.beanplanet.core.util.StringUtil.isBlank;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">Media Type</a>, as defined by the <a href="https://www.iana.org">Internet Assigned Numbers Authority (IANA)</a>. Represents
 * most widely-known types of content such as text (text/plain), HTML (text/html), images (image/png amongst others) and more.
 */
public class MediaType implements Named {
    /**
     * The base type of the media, such as "text" in "text/html".
     */
    private final String type;
    /**
     * The sub-type of the media, such as "html" in "text/html".
     */
    private final String subtype;
    /**
     * Parameters associated with the media type.
     */
    private final Parameters parameters;

    private final String canonicalName;

    public MediaType(final String type,
                     final String subtype,
                     final Parameters parameters) {
        this.type = type;
        this.subtype = subtype;
        this.parameters = parameters == null ? Parameters.empty() : parameters;

        this.canonicalName = type + "/" + subtype;
    }

    public MediaType(final String type,
                     final String subtype) {
        this(type, subtype, null);
    }

    public MediaType(final String fullSpec) {
        List<String> typeAndParams = StringUtil.asDsvList(fullSpec, ";");
        Assert.assertFalse(typeAndParams.isEmpty(), "Media type may not be empty");

        final String typeAndSubtype = typeAndParams.get(0).trim();
        int slashPos = typeAndSubtype.indexOf('/');

        this.type = fullSpec.substring(0, slashPos <= 0 ? typeAndSubtype.length() : slashPos);
        this.subtype = slashPos <= 0 ? "*" : typeAndSubtype.substring(slashPos + 1);

        this.parameters = typeAndParams.size() == 1
                ? Parameters.empty()
                : new Parameters(typeAndParams.subList(1, typeAndParams.size())
                                              .stream()
                                              .map(p -> {
                                                  int equalPos = p.indexOf('=');
                                                  final String name = p.substring(0, equalPos <= 0 ? p.length() : equalPos);
                                                  final String value = equalPos <= 0 ? "" : p.substring(equalPos + 1);
                                                  return Pair.of(name, StringUtil.trim(value, "\""));
                                              })
                                              .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        this.canonicalName = type + "/" + subtype;
    }

    /**
     * Gets the base of the media type name. For the JPEG image content type, <b>image</b>/jpeg, the base name is
     * <b>image</b>.
     *
     * @return the base name part of the media type name.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the subtype name of the media type name. For the JPEG image content type, image/<b>jpeg</b>, the base
     * name is <b>jpeg</b>.
     *
     * @return the subtype name part of the media type name.
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * Determined if this media type has associated parameters.
     *
     * @return true if this media type has one or more parameters, false if none.
     */
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    /**
     * Gets the parameters associated with the media type.
     *
     * @return any parameters associated with the media type, which may be empty but never null.
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Gets the complete name of this media type in the canonical form "<type>/<subtype>", such as "text/plain".
     *
     * @return the full media type name.
     */
    public String getName() {
        return canonicalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MediaType that = (MediaType) o;
        return Objects.equals(this.type, that.type)
                && Objects.equals(this.subtype, that.subtype)
                && Objects.equals(this.parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subtype, parameters);
    }

    public String getCanonicalForm() {
        StringBuilder s = new StringBuilder();
        s.append(getName());

        if (hasParameters()) {
            s.append("; ").append(parameters.stream().map(p -> p.getLeft() + "=" + quoteParamValueIfNeeded(p.getRight())).collect(Collectors.joining("; ")));
        }

        return s.toString();
    }

    private String quoteParamValueIfNeeded(String parameterValue) {
        if (isBlank(parameterValue)) return "";
        return StringUtil.containsAnyOf(parameterValue, "()<>@,;:\\\"/[]?={} \t") ? "\"" + parameterValue + "\"" : parameterValue;

    }

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this, "canonicalForm", this::getCanonicalForm).build();
    }

    /**
     * Convenience method that creates a new Media Type, copied from this instance and with the given charset parameter.
     *
     * @param charset the character set with which to set as parameter on the new media type.
     * @return a new media type, copied from this instance, with the <code<charset</code> parameter set to that specified.
     */
    public MediaType charset(final Charset charset) {
        return new MediaType(type, subtype, Parameters.singleton("charset", charset.name()));
    }
}

