package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.net.Base64Decoder;
import org.beanplanet.core.net.Base64Encoder;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.MediaTypes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.beanplanet.core.util.StringUtil.isBlank;

/**
 * Implementation of a resource backed by an <a href="">RFC2397 - The Data "URL" scheme</a> - supporting URLs encoded
 * with small data items.
 */
public class DataUrlResource extends UrlResource {
    public static final String DATA_URL_SCHEME = "data";
    public static final String DATA_URL_SCHEME_PROTOCOL = DATA_URL_SCHEME + ":";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
    public static final MediaType DEFAULT_MEDIA_TYPE = MediaTypes.Text.PLAIN;
    public static final MediaType DEFAULT_MEDIA_TYPE_WITH_CHARSET = MediaTypes.Text.PLAIN.charset(DEFAULT_CHARSET);
    private final DataUrlElements dataUrlElements;

    public DataUrlResource(final URL url) {
        super(url);
        dataUrlElements = parseDataUrl(url.toExternalForm());
    }

    public DataUrlResource(final String url) {
        super(url);
        dataUrlElements = parseDataUrl(url);
    }

    public static DataUrlResourceBuilder builder() {
        return new DataUrlResourceBuilder();
    }

    public static class DataUrlResourceBuilder {
        private DataUrlElements dataUrlElements = new DataUrlElements();

        public DataUrlResourceBuilder mediaType(final MediaType mediaType) {
            dataUrlElements.mediaType = mediaType;
            return this;
        }

        public DataUrlResourceBuilder charset(final Charset charset) {
            dataUrlElements.charset = charset;
            return this;
        }

        public DataUrlResourceBuilder charset(final String charset) {
            return charset(Charset.forName(charset));
        }

        public DataUrlResourceBuilder base64Encoded() {
            return base64Encoded(true);
        }

        public DataUrlResourceBuilder urlEncoded() {
            return base64Encoded(false);
        }

        public DataUrlResourceBuilder base64Encoded(final boolean base64Encoded) {
            dataUrlElements.base64Encoded = base64Encoded;
            return this;
        }

        public DataUrlResourceBuilder data(final Resource data) {
            dataUrlElements.data = data;
            return this;
        }

        public DataUrlResourceBuilder data(final CharSequence data) {
            return data(new CharSequenceResource(data));
        }

        public DataUrlResourceBuilder data(final byte[] data) {
            return data(new ByteArrayResource(data));
        }

        public DataUrlResource build() {
            return new DataUrlResource(dataUrlElements.toUrl());
        }
    }


    private static DataUrlElements parseDataUrl(final String url) {
        if (!url.startsWith(DATA_URL_SCHEME_PROTOCOL)) {
            throw new IllegalArgumentException("RFC2397 data URLs must have a \"" + DATA_URL_SCHEME_PROTOCOL + "\" scheme [" + url + "]");
        }

        int commaIndex = url.indexOf(',');
        if (commaIndex < 0) {
            throw new IllegalArgumentException("RFC2397 data URL [" + url + "] does not have any data separated by a  comma (,)");
        }

        String metadata = url.substring(5, commaIndex);
        final String data = url.substring(commaIndex + 1);

        final String[] parts = metadata.split(";");
        String mediatypeStr = parts.length > 0 && !parts[0].isEmpty() ? parts[0] : null;

        boolean isBase64Encoded = false;
        Charset charset = null;
        for (String part : parts) {
            if ("base64".equals(part.trim())) {
                isBase64Encoded = true;
            } else if (part.indexOf("=") >= 0) {
                int equalsPos = part.indexOf("=");
                String name = part.substring(0, equalsPos).trim();
                String value = part.substring(equalsPos + 1).trim();
                if ("charset".equalsIgnoreCase(name)) {
                    charset = Charset.forName(value);
                }
            }
        }

        if (isBlank(mediatypeStr)) {
            mediatypeStr = DEFAULT_MEDIA_TYPE.getName();
            charset = DEFAULT_CHARSET;
        }
        final Charset charsetOrDefault = charset == null ? Charset.defaultCharset() : charset;

        MediaType mediaType = new MediaType(mediatypeStr + (charset != null && MediaTypes.isText(mediatypeStr) ? "; charset=" + charset.name() : ""));
        byte[] byteData = isBase64Encoded ? Base64Decoder.decode(data) : urlPercentDecode(data, charsetOrDefault).getBytes(charsetOrDefault);
        return new DataUrlElements(mediaType, isBase64Encoded, charset, new ByteArrayResource(byteData));
    }

    /**
     * Returns the character set associated with the data URL character data.
     *
     * @return any character set associated with the data URL, or empty if none was provided; an empty character set
     * implies a default of {@link StandardCharsets#US_ASCII}.
     */
    public Optional<Charset> getCharset() {
        return Optional.ofNullable(dataUrlElements.charset);
    }

    /**
     * Returns the character set associated with the data URL character data.
     *
     * @return the character set associated with the data URL, never null; if not set, the default
     * of {@link StandardCharsets#US_ASCII}.
     */
    public Charset getCharsetOrDefault() {
        return getCharset().orElse(DEFAULT_CHARSET);
    }

    /**
     * Returns the media type associated with the data URL character data.
     *
     * @return the media type associated with the data URL character data, which may be empty; an empty media type
     * implies a default of {@link MediaTypes.Text#PLAIN}.
     */
    public Optional<MediaType> getMediaType() {
        return Optional.ofNullable(dataUrlElements.mediaType);
    }

    /**
     * Returns the media type associated with the data URL character data, if any, or the default.
     *
     * @return the media type associated with the data URL character data, never null; if not set, the default
     * of {@link MediaTypes.Text#PLAIN}.
     */
    public MediaType getMediaTypeOrDefault() {
        return getMediaType().orElse(DEFAULT_MEDIA_TYPE);
    }

    /**
     * Returns an input stream backed by the raw byte data decoded from the associated data URL.
     *
     * @return an input stream backed by the data URL decoded data.
     */
    @Override
    public InputStream getInputStream() {
        return dataUrlElements.data.getInputStream();
    }

    /**
     * Creates a reader backed by raw byte data decoded from the associated data URL and any character set associated
     * with the data URL.
     *
     * @return a newly created reader backed by the data URL decoded data and any associated character set.
     */
    public Reader getReader() throws IoException {
        return new InputStreamReader(getInputStream(), dataUrlElements.getCharsetOrDefault());
    }

    private static class DataUrlElements {
        /**
         * The media type of the data URL.
         */
        private MediaType mediaType;
        /**
         * Whether the data in this data URl is Base64 encoded.
         */
        private boolean base64Encoded;
        /**
         * The character set associated with the data, where the data is textual.
         */
        private Charset charset;
        /**
         * The raw data encoded within the data URL.
         */
        private Resource data;

        DataUrlElements(final MediaType mediaType,
                        final boolean base64Encoded,
                        final Charset charset,
                        final Resource data) {
            this.mediaType = mediaType;
            this.base64Encoded = base64Encoded;
            this.charset = charset;
            this.data = data;
        }

        private DataUrlElements() {
        }

        private URL toUrl() {
            StringBuilder s = new StringBuilder();
            MediaType mediaTypeWithCharset = mediaType;
            if (mediaType == null) {
                if (charset != null) {
                    mediaTypeWithCharset = DEFAULT_MEDIA_TYPE.charset(charset);
                }
            } else if (mediaType.getParameters().hasCharset()) {
                if (charset != null) {
                    mediaTypeWithCharset = mediaType.charset(charset);
                }
            } else {
                if (charset != null) {
                    mediaTypeWithCharset = mediaType.charset(charset);
                }
            }

            s.append(DATA_URL_SCHEME_PROTOCOL)
             .append(mediaTypeWithCharset.getCanonicalForm().replace(" ", ""))
             .append(base64Encoded ? ";base64" : "")
             .append(data != null ? "," : "")
             .append(data != null ? encodeData() : "");
            try {
                return URI.create(s.toString()).toURL();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Data Url is invalid [" + s.toString() + "]: " + e.getMessage(), e);
            }
        }

        private String encodeData() {
            return base64Encoded ? Base64Encoder.encode(data) : urlPercentEncode(data.readFullyAsString(getCharsetOrDefault()));
        }

        private Charset getCharsetOrDefault() {
            return charset != null ? charset : Charset.defaultCharset();
        }

        private String urlPercentEncode(final String str) {
//            return urlPercentEncode(data.readFullyAsString(getCharsetOrDefault()), getCharsetOrDefault()).replace("+", "%20");
            return DataUrlResource.urlPercentEncode(data.readFullyAsString(getCharsetOrDefault()), getCharsetOrDefault());
        }
    }

    private static String urlPercentEncode(final String str, final Charset charset) {
        return URLEncoder.encode(str, charset == null ? Charset.defaultCharset() : charset).replace("+", "%20");
    }

    private static String urlPercentDecode(final String str, final Charset charset) {
        return URLDecoder.decode(str, charset == null ? Charset.defaultCharset() : charset);
    }
}
