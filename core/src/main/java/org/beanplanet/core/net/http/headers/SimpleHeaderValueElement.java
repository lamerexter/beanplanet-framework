package org.beanplanet.core.net.http.headers;

import org.beanplanet.core.models.NameValue;

import java.util.Objects;

public class SimpleHeaderValueElement implements HeaderValueElement, Cloneable {
    private final String name;
    private final String value;
    private final NameValue<String>[] parameters;

    public SimpleHeaderValueElement(String name, String value, NameValue<String>[] parameters) {
        this.name = name;
        this.value = value;
        if (parameters != null) {
            this.parameters = parameters;
        } else {
            this.parameters = new NameValue[0];
        }

    }

    public SimpleHeaderValueElement(String name, String value) {
        this(name, value, (NameValue<String>[])null);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public NameValue<String>[] getParameters() {
        return (NameValue<String>[])this.parameters.clone();
    }

    public int getParameterCount() {
        return this.parameters.length;
    }

    public NameValue<String> getParameter(int index) {
        return this.parameters[index];
    }

    public NameValue<String> getParameter(String name) {
        NameValue<String> found = null;
        NameValue<String>[] arr$ = this.parameters;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            NameValue<String> current = arr$[i$];
            if (current.getName().equalsIgnoreCase(name)) {
                found = current;
                break;
            }
        }

        return found;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof HeaderValueElement)) {
            return false;
        }

            SimpleHeaderValueElement that = (SimpleHeaderValueElement)object;
            return Objects.equals(this.name, that.name)
                    && Objects.equals(this.value, that.value)
                    && Objects.equals(this.parameters, that.parameters);
    }

    public int hashCode() {
        return Objects.hash(name, value, parameters);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name);
        if (this.value != null) {
            buffer.append("=");
            buffer.append(this.value);
        }

        NameValue<String>[] arr$ = this.parameters;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            NameValue<String> parameter = arr$[i$];
            buffer.append("; ");
            buffer.append(parameter);
        }

        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
