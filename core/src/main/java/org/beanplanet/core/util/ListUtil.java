package org.beanplanet.core.util;

import java.util.Collections;
import java.util.List;

/**
 * A utility class containing useful list functionality.
 *
 * @author Chris Taylor
 */
public class ListUtil {
    public static <T> List<T> moveListElement(final List<T> list, final int fromIndex, final int toIndex) {
        Collections.swap(list, fromIndex, toIndex);
        return list;
    }
}
