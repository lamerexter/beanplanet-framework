package org.beanplanet.core.net;

import org.beanplanet.core.io.resource.DataUrlResource;
import org.beanplanet.core.net.http.HttpHeaders;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.MediaTypes;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataUrlConnection extends URLConnection {

    private static final Charset DEFAULT_CONTENT_CHARSET = StandardCharsets.US_ASCII;
    private static final MediaType DEFAULT_MEDIATYPE = MediaTypes.Text.PLAIN;

    private final DataUrlResource dataUrlResource;

    public DataUrlConnection(final URL url) throws MalformedURLException {
        super(url);
        this.dataUrlResource = new DataUrlResource(url);
    }

    private static boolean isText(String mediatype) {
        return mediatype != null && (mediatype.startsWith("text/") || mediatype.endsWith("+xml"));
    }

    @Override
    public void connect() {
        this.connected = true;
    }

    @Override
    public InputStream getInputStream() {
        return dataUrlResource.getInputStream();
    }

    @Override
    public String getContentType() {
        return dataUrlResource.getMediaTypeOrDefault().getCanonicalForm();
    }

    @Override
    public String getHeaderField(String name) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return String.valueOf(dataUrlResource.getContentLength());
        }

        return null;
    }
}
