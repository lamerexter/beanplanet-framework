package org.beanplanet.core.models;

import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TripleTest {
    @Test
    public void givenATriple_whenCreated_thenTripleConsistsOfTheCorrectValues() {
        assertThat(Triple.of("stringValue", 123, Boolean.TRUE).getFirst(), equalTo("stringValue"));
        assertThat(Triple.of("stringValue", 123, Boolean.TRUE).getSecond(), equalTo(123));
        assertThat(Triple.of("stringValue", 123, Boolean.TRUE).getThird(), equalTo(Boolean.TRUE));

        assertThat(Triple.tripleOf("stringValue", 123, Boolean.TRUE).firstIsNotNull(), is(true));
        assertThat(Triple.tripleOf("stringValue", 123, Boolean.TRUE).secondIsNotNull(), is(true));
        assertThat(Triple.tripleOf("stringValue", 123, Boolean.TRUE).thirdIsNotNull(), is(true));

        assertThat(Triple.tripleOf(null, 123, Boolean.TRUE).firstIsNotNull(), is(false));
        assertThat(Triple.tripleOf("stringValue", null, Boolean.TRUE).secondIsNotNull(), is(false));
        assertThat(Triple.tripleOf("stringValue", 123, null).thirdIsNotNull(), is(false));
    }

    @Test
    public void givenTwoTriples_whenTestedForEquality_thenTripleEqualityCheckIsCorrect() {
        assertThat(Triple.tripleOf("stringValue", 123, Boolean.TRUE), equalTo(Triple.tripleOf("stringValue", 123, Boolean.TRUE)));
        assertThat(new Triple<>(), equalTo(Triple.tripleOf(null, null, null)));

        assertThat(Triple.tripleOf(null, 123, Boolean.TRUE), not(equalTo(Triple.tripleOf("stringValue", 123, Boolean.TRUE))));
        assertThat(Triple.tripleOf("stringValue", null, Boolean.TRUE), not(equalTo(Triple.tripleOf("stringValue", 123, Boolean.TRUE))));
        assertThat(Triple.tripleOf("stringValue", 123, null), not(equalTo(Triple.tripleOf("stringValue", 123, Boolean.TRUE))));
    }

    @Test
    public void givenATriple_whenHashed_thenHashedValuesAreCorrectValues() {
        assertThat(Triple.tripleOf("stringValue", 123, Boolean.TRUE).hashCode(), equalTo(Objects.hash("stringValue", 123, Boolean.TRUE)));

        assertThat(Triple.tripleOf(null, 123, Boolean.TRUE).hashCode(), not(equalTo(Objects.hash("stringValue", 123, Boolean.TRUE))));
        assertThat(Triple.tripleOf("stringValue", null, Boolean.TRUE).hashCode(), not(equalTo(Objects.hash("stringValue", 123, Boolean.TRUE))));
        assertThat(Triple.tripleOf("stringValue", 123, null).hashCode(), not(equalTo(Objects.hash("stringValue", 123, Boolean.TRUE))));
    }
}