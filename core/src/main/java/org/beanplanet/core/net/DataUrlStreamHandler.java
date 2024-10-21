package org.beanplanet.core.net;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class DataUrlStreamHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(final URL url) throws MalformedURLException {
        return new DataUrlConnection(url);
    }
}
