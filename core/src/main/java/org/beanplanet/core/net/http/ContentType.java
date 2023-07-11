package org.beanplanet.core.net.http;

import org.beanplanet.core.mediatypes.MediaType;
import org.beanplanet.core.mediatypes.SimpleMediaType;
import org.beanplanet.core.lang.ParseException;
import org.beanplanet.core.models.NameValue;
import org.beanplanet.core.net.http.headers.DefaultHeaderValueParser;
import org.beanplanet.core.net.http.headers.HeaderValueElement;
import org.beanplanet.core.net.http.headers.ParserCursor;
import org.beanplanet.core.util.CharArrayBuffer;
import org.beanplanet.core.util.StringUtil;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * HTTP content type information, consisting of media type and optional character set and parameters.
 */
public class ContentType {
    private final MediaType mediaType;
    private final Charset charset;
    private final NameValue<String>[] nameValues;

    protected ContentType() {
        this(null, null);
    }

    private ContentType(final MediaType mediaType, final Charset charset, NameValue<String>[] nameValues) {
        this.mediaType = mediaType;
        this.charset = charset;
        this.nameValues = nameValues;
    }

    private ContentType(final String mediaType, final String charset) {
        this(new SimpleMediaType(mediaType), StringUtil.isEmptyOrNull(charset) ? null : Charset.forName(charset), null);
    }

    public static ContentType from(final MediaType mimeType) {
        return new ContentType(mimeType, null, null);
    }

    public static ContentType from(final MediaType mimeType, final Charset charset, final NameValue<String>[] params) {
        return new ContentType(mimeType, charset, params);
    }

    public static ContentType from(final String mediaType) {
        return from(mediaType, null);
    }

    public static ContentType from(final String mediaType, final String charset) {
        return new ContentType(mediaType, charset);
    }

    private static ContentType from(String mimeType, NameValue<String>[] params, boolean strict) {
        Charset charset = null;
        if (params != null) {
            int len$ = params.length;

            for (int n = 0; n < len$; ++n) {
                NameValue<String> param = params[n];
                if (param.getName().equalsIgnoreCase("charset")) {
                    String s = param.getValue();
                    if (StringUtil.isEmptyOrNull(s)) {
                        break;
                    }

                    try {
                        charset = Charset.forName(s);
                        break;
                    } catch (UnsupportedCharsetException var10) {
                        if (!strict) {
                            break;
                        }

                        throw var10;
                    }
                }
            }
        }

        return from(new SimpleMediaType(mimeType), charset, params);
    }

    public static ContentType from(final HttpMessage httpMessage) {
        String contentTypeHeader = httpMessage.getLastHeader(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeader == null) return null;

        return parse(contentTypeHeader);
    }

    public static ContentType parse(final CharSequence charSequence) {
        CharArrayBuffer buf = new CharArrayBuffer(charSequence.length());
        buf.append(charSequence);
        ParserCursor cursor = new ParserCursor(0, charSequence.length());
        HeaderValueElement[] elements = DefaultHeaderValueParser.getInstance().parseElements(buf, cursor);
        if (elements.length > 0) {
            return from(elements[0].getName());
        } else {
            throw new ParseException("Invalid content type: " + charSequence);
        }

    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Charset getCharset() {
        return charset;
    }

    public NameValue<String>[] getNameValues() {
        return nameValues;
    }
}
