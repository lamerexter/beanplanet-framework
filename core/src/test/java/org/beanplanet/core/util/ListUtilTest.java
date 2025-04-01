package org.beanplanet.core.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.beanplanet.core.util.ListUtil.moveListElement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListUtilTest {
    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_emptyList() {
        moveListElement(new ArrayList<>(), 1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_whenListSourceIndexExceedsBounds_thenExceptionIsThrown() {
        moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 2, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_whenListDestinationIndexExceedsBounds_thenExceptionIsThrown() {
        moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 0, 2);
    }

    @Test
    public void moveListElement_whenIndicesAreWithinBounds_thenExceptionIsThrown() {
        assertThat(moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 1,  0), equalTo(Arrays.asList("b", "a")));
    }
}