package org.beanplanet.core.models;

import org.junit.Test;

import static org.beanplanet.core.models.Pair.pairOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class PairTest {
    @Test
    public void givenAPair_andAnotherPairOfTheSameItems_whenEqualsIsInvoked_thenThePairsAreEqual() {
        assertThat(pairOf(11, 22), equalTo(pairOf(11, 22)));
    }

    @Test
    public void givenAPair_andAnotherPairOfTheSameItemsInADifferentOrder_whenEqualsIsInvoked_thenThePairsAreNotEqual() {
        assertThat(pairOf(11, 22), is(not(equalTo(pairOf(22, 11)))));
        assertThat(new Pair<>(11), is(not(equalTo(pairOf(22, 11)))));
        assertThat(new Pair<>(22), is(not(equalTo(pairOf(22, 11)))));
        assertThat(new Pair<>(), is(not(equalTo(pairOf(22, 11)))));
    }

    @Test
    public void givenAPair_andAnotherPairOfTheSameItems_whenHashCodeIsInvoked_thenTheHashCodesAreEqual() {
        assertThat(pairOf(11, 22).hashCode(), equalTo(pairOf(11, 22).hashCode()));
    }

    @Test
    public void givenAPair_andAnotherPairOfDifferentItems_whenHashCodeIsInvoked_thenTheHashCodesAreNotEqual() {
        assertThat(pairOf(22, 11).hashCode(), is(not(equalTo(pairOf(11, 22).hashCode()))));
    }

    @Test
    public void givenAPairWithNullLeft_whenEachSideIsCheckedForNull_thenTheLeftSideIsIdentifiedAsNull() {
        assertThat(pairOf(null, 11).leftIsNotNull(), is(false));
        assertThat(pairOf(null, 11).rightIsNotNull(), is(true));
    }

    @Test
    public void givenAPairWithNullRight_whenEachSideIsCheckedForNull_thenTheRightSideIsIdentifiedAsNull() {
        assertThat(pairOf(11, null).rightIsNotNull(), is(false));
        assertThat(pairOf(11, null).leftIsNotNull(), is(true));
    }
}