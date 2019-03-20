package org.beanplanet.core.io;

import static org.beanplanet.core.io.Path.EMPTY_PATH;

public class PathUtil {
    @SuppressWarnings("unchecked")
    public static final <T> Path<T> emptyPath() {
        return (Path<T>)EMPTY_PATH;
    }
}
