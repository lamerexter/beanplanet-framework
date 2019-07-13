/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.cache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ManagedCacheTest {
    private ManagedCache<String, String> cache;

    @Mock
    private CacheListener<String, String> listener;

    @Before
    public void setUp() {
        cache = new ManagedCache<>(); // Mockito @InjectMocks gets confused with which constructor to call!
    }

    @Test
    public void put_itemAdded() {
        // Given
        final String key = "theKey";
        final String value = "theValue";
        final String nextValue = "theNextValue";
        cache.addCacheListener(listener);

        // When
        cache.put(key, value);

        // Then the item is added and event dispatched
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey(key), is(true));
        assertThat(cache.get(key), sameInstance(value));

        ArgumentCaptor<CacheItemsAddedEvent> eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(1)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getKeysAffected(), equalTo(Collections.singletonList(key)));
        assertThat(eventCapture.getValue().getValuesEvicted(), equalTo(Collections.emptyList()));
        assertThat(eventCapture.getValue().getValuesAffected(), equalTo(Collections.singletonList(value)));

        // When another item is added with the same key
        cache.put(key, nextValue);

        // Then the item is added and event dispatched with both old and new values
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey(key), is(true));
        assertThat(cache.get(key), sameInstance(value));

        eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(2)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getKeysAffected(), equalTo(Collections.singletonList(key)));
        assertThat(eventCapture.getValue().getValuesEvicted(), equalTo(Collections.singletonList(value)));
        assertThat(eventCapture.getValue().getValuesAffected(), equalTo(Collections.singletonList(nextValue)));
    }

}