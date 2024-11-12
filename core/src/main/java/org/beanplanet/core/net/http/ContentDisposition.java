package org.beanplanet.core.net.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

import static org.beanplanet.core.net.http.MimeCodec.*;

/**
 * Representation and builder of the <code>Content-Disposition</code> HTTP header, used in responses, as defined in
 * <a href="https://www.rfc-editor.org/rfc/rfc6266">RFC 6266</a>.
 */
public final class ContentDisposition {
    private final String dispositionType;

    private final String name;

    private final String filename;

    private final Charset charset;

    private ContentDisposition(final ContentDispositionBuilder builder) {
        this.dispositionType = builder.dispositionType;
        this.name = builder.name;
        this.filename = builder.filename;
        this.charset = builder.charset;
    }

    public static ContentDispositionTypeBuilder builder() {
        return new ContentDispositionTypeBuilder();
    }

    public String getCanonicalForm() {
        StringBuilder sb = new StringBuilder();
        if (this.dispositionType != null) {
            sb.append(this.dispositionType);
        }
        if (this.name != null) {
            sb.append("; name=\"").append(this.name).append('\"');
        }
        if (this.filename != null) {
            if (this.charset == null || StandardCharsets.US_ASCII.equals(this.charset)) {
                sb.append("; filename=\"");
                sb.append(encodeQuotedPrintableCharacters(this.filename)).append('\"');
            }
            else {
                sb.append("; filename=\"");
                sb.append(encodeQuotedPrintable(this.filename, this.charset)).append('\"');
                sb.append("; filename*=");
                sb.append(rfc6266EncodeHttpHeaderParameter(this.filename, this.charset));
            }
        }
        return sb.toString();
    }

    public String toString() {
        return getCanonicalForm();
    }

    public static class ContentDispositionTypeBuilder {
        public ContentDispositionBuilder formData() {
            return new ContentDispositionBuilder("form-data");
        }
    }

    public static class ContentDispositionBuilder {
        private String dispositionType;

        private String name;

        private String filename;

        private Charset charset;

        private ContentDispositionBuilder(final String dispositionType) {
            this.dispositionType = dispositionType;
        }

        public ContentDispositionBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public ContentDispositionBuilder filenameIf(Predicate<String> condition, String filename, Charset charset) {
            if (condition.test(filename)) {
                this.filename = filename;
                this.charset = charset;
            }

            return this;
        }

        public ContentDisposition build() {
            return new ContentDisposition(this);
        }

    }
}
