package org.beanplanet.core.net.http.converter;

public interface HttpMessageBodyConverterRegistryAware {
    HttpMessageBodyConverterRegistry setRegistry(HttpMessageBodyConverterRegistry registry);
}
