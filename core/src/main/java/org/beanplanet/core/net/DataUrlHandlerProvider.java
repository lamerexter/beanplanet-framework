package org.beanplanet.core.net;

import org.beanplanet.core.io.resource.DataUrlResource;

import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

public class DataUrlHandlerProvider extends URLStreamHandlerProvider {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if(DataUrlResource.DATA_URL_SCHEME.equals(protocol)){
            return new DataUrlStreamHandler();
        }

        return null;
    }
}
