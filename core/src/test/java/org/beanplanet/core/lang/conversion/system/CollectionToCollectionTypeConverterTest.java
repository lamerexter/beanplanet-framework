package org.beanplanet.core.lang.conversion.system;

import org.junit.Test;

import java.util.LinkedHashSet;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.conversion.system.CollectionToCollectionTypeConverter.listToLinkedHashSetConversion;
import static org.beanplanet.core.lang.conversion.system.CollectionToCollectionTypeConverter.setToListConversion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class CollectionToCollectionTypeConverterTest {
    @Test
    public void givenANullListOfValues_whenTheListIsConvertedToASet_thenNullIsReturned() {
        assertThat(listToLinkedHashSetConversion(null), nullValue());
    }

    @Test
    public void givenAListOfValues_whenTheListIsConvertedToASet_thenASetOfValuesIsReturnedInTheSameOrder() {
        assertThat(listToLinkedHashSetConversion(asList(5, 1, 2, 3, 2, 4)), equalTo(new LinkedHashSet<>(asList(5, 1, 2, 3, 2, 4))));
    }

    @Test
    public void givenANullSetOfValues_whenTheSetIsConvertedToAList_thenNullIsReturned() {
        assertThat(setToListConversion(null), nullValue());
    }

    @Test
    public void givenASetOfValues_whenTheSetIsConvertedToASet_thenAListOfValuesIsReturnedInTheSameOrder() {
        assertThat(setToListConversion(new LinkedHashSet<>(asList(5, 1, 2, 3, 2, 4))), equalTo(asList(5, 1, 2, 3, 4)));
    }
}