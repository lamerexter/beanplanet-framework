package org.beanplanet.core.models.path;

import org.beanplanet.core.util.StringUtil;

/**
 * The path component of a URI, following the specification of URIs outlined in
 * <a href="https://www.rfc-editor.org/rfc/rfc3986">URI: General Syntax</a>
 */
public class UriPath extends DelimitedNamePath {
    final boolean isAbsolute;

    public UriPath(String path) {
        super(StringUtil.lTrim(path, "/"), "/");
        this.isAbsolute = path != null && path.startsWith("/");
    }

    /**
     * Whether this path is empty.
     *
     * @return true if this path is absolute or contains path elments, false otherwise.
     */
    public boolean isEmpty() {
        return !isAbsolute && super.isEmpty();
    }

    @Override
    public boolean isAbsolute() {
        return isAbsolute;
    }
}
