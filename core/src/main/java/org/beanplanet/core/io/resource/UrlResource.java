package org.beanplanet.core.io.resource;

import java.net.URI;
import java.net.URL;

public class UrlResource extends UriResource {
    public UrlResource() {}

    public UrlResource(String url) {
        super(url);
    }

    public UrlResource(URL url) {
        super(URI.create(url.toExternalForm()));
    }
}
