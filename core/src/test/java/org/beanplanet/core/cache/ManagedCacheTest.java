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

import org.beanplanet.core.cache.ManagedCache.ManagedCacheEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ManagedCacheTest {
    private ManagedCache<String, String> cache;

    @Mock
    private CacheListener<String, String> listener;

    @Before
    public void setUp() {
        cache = new ManagedCache<>(); // Mockito @InjectMocks gets confused with which constructor to call!
        cache.addCacheListener(listener);
    }

    @Test
    public void put_itemAdded() {
        // Given
        final String key = "theKey";
        final String value = "theValue";
        final String nextValue = "theNextValue";

        // When
        cache.put(key, value);

        // Then the item is added and event dispatched
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey(key), is(true));
        assertThat(cache.get(key), sameInstance(value));

        ArgumentCaptor<CacheItemsAddedEvent> eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(1)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getEntriesEvicted(), equalTo(emptyList()));
        assertThat(eventCapture.getValue().getEntriesAdded(), equalTo(singletonList(new ManagedCacheEntry<>(key, value))));

        // When another item is added with the same key
        cache.put(key, nextValue);

        // Then the item is added and event dispatched with both old and new values
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey(key), is(true));
        assertThat(cache.get(key), sameInstance(nextValue));

        eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(2)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getEntriesEvicted(), equalTo(singletonList(new ManagedCacheEntry<>(key, value))));
        assertThat(eventCapture.getValue().getEntriesAdded(), equalTo(singletonList(new ManagedCacheEntry<>(key, nextValue))));
    }

    @Test
    public void putAll_itemsAdded() {
        // Given
        final String key1 = "key1";
        final String value1 = "value1";
        final String key2 = "key2";
        final String originalValue2 = "originalValue2";
        final String otherValue2 = "value2";
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put(key1, value1);
        otherMap.put(key2, otherValue2);

        // When previous key/values exist
        cache.put(key2, originalValue2);

        // Then the item is added and event dispatched
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey(key2), is(true));
        assertThat(cache.get(key2), sameInstance(originalValue2));

        ArgumentCaptor<CacheItemsAddedEvent> eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(1)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getEntriesEvicted(), equalTo(emptyList()));
        assertThat(eventCapture.getValue().getEntriesAdded(), equalTo(singletonList(new ManagedCacheEntry<>(key2, originalValue2))));

        // When multiple items are added
        cache.putAll(otherMap);

        // Then
        assertThat(cache.isEmpty(), is(false));
        assertThat(cache.size(), is(2));
        assertThat(cache.containsKey(key1), is(true));
        assertThat(cache.containsKey(key2), is(true));
        assertThat(cache.get(key1), sameInstance(value1));
        assertThat(cache.get(key2), sameInstance(otherValue2));

        eventCapture = ArgumentCaptor.forClass(CacheItemsAddedEvent.class);
        verify(listener, times(2)).onCacheItemsAdded(eventCapture.capture());
        assertThat(eventCapture.getValue().getEntriesEvicted(), equalTo(singletonList(new ManagedCacheEntry<>(key2, originalValue2))));
        assertThat(eventCapture.getValue().getEntriesAdded(), equalTo(asList(
                new ManagedCacheEntry<>(key1, value1),
                new ManagedCacheEntry<>(key2, otherValue2))
        ));
    }

    @Test
    public void get_cacheMiss() {
        // Given
        final String key = "theKey";

        // WHen
        cache.get(key);

        // Then
        assertThat(cache.isEmpty(), is(true));

        ArgumentCaptor<CacheMissEvent> eventCapture = ArgumentCaptor.forClass(CacheMissEvent.class);
        verify(listener, times(1)).onCacheMiss(eventCapture.capture());
        assertThat(eventCapture.getValue().getKey(), equalTo(key));
    }

    @Test
    public void get_cacheHit() {
        // Given
        final String key = "theKey";
        final String value = "theValue";
        cache.put(key, value);

        // WHen
        cache.get(key);

        // Then
        assertThat(cache.isEmpty(), is(false));

        ArgumentCaptor<CacheHitEvent> eventCapture = ArgumentCaptor.forClass(CacheHitEvent.class);
        verify(listener, times(1)).onCacheHit(eventCapture.capture());
        assertThat(eventCapture.getValue().getKey(), equalTo(key));
        assertThat(eventCapture.getValue().getValue(), equalTo(value));
    }

    @Test
    public void remove() {
        // Given
        final String key = "theKey";
        final String value = "theValue";
        cache.put(key, value);

        // When remove non-existent entry
        cache.remove("noEntryForThisKey...");

        // Then
        assertThat(cache.isEmpty(), is(false));

        verify(listener).onCacheItemsAdded(any());
        verifyNoMoreInteractions(listener);

        // When remove existing entry
        cache.remove(key);

        // Then
        ArgumentCaptor<CacheItemsRemovedEvent> eventCapture = ArgumentCaptor.forClass(CacheItemsRemovedEvent.class);
        verify(listener, times(1)).onCacheItemsRemoved(eventCapture.capture());

        assertThat(eventCapture.getValue().getEntriesRemoved(), equalTo(Collections.singletonList(
                new ManagedCacheEntry<>(key, value))
        ));
    }

    @Test
    public void clear() {
        // Given
        final String key = "theKey";
        final String value = "theValue";
        cache.put(key, value);

        // When the cache is cleared
        cache.clear();

        // Then
        assertThat(cache.isEmpty(), is(true));

        verify(listener).onCacheItemsAdded(any());
        verify(listener).onCacheCleared(any());
        verifyNoMoreInteractions(listener);
    }
}