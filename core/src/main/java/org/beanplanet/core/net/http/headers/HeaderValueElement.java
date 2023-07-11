package org.beanplanet.core.net.http.headers;

import org.beanplanet.core.models.NameValue;

public interface HeaderValueElement {
    String getName();

    String getValue();

    NameValue<String>[] getParameters();

    int getParameterCount();

    NameValue<String> getParameter(String name);
    NameValue<String> getParameter(int param);
}
