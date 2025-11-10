/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.core.streams;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.beanplanet.core.streams.StreamUtil.batchCollector;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BatchCollectorTest {

    @Test
    public void givenAStreamOfItems_whenCollectedInBatches_thenBatchesOfTheGivenSizeOrLessAreProcessedCorrectly() {
        // Given
        List<Integer> allItems = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> batches = new ArrayList<>();
        List<Integer> aggregatedBatches = new ArrayList<>();

        // When
        List<Integer> result = allItems.stream().collect(batchCollector(4, batch -> {
            batches.add(new ArrayList<>(batch));
            aggregatedBatches.addAll(batch);
        }));

        // Then
        assertThat(batches.size(), equalTo(3));
        assertThat(batches.get(0).size(), equalTo(4));
        assertThat(batches.get(1).size(), equalTo(4));
        assertThat(batches.get(2).size(), equalTo(2));
        assertThat(aggregatedBatches, equalTo(allItems));
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void givenAStreamOfItems_whenCollectedInBatchesInParallel_thenBatchesAreDispatchedCorrectly() {
        // Given
        List<Integer> allItems = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> aggregatedBatches = new CopyOnWriteArrayList<>();

        // When
        List<Integer> result = allItems.parallelStream()
                .collect(batchCollector(3, aggregatedBatches::addAll));

        // Then
        aggregatedBatches.sort(Integer::compareTo);
        assertThat(aggregatedBatches, equalTo(allItems));
    }

    @Test
    public void givenAStreamOfItems_andTotalWhollyDivisibleByBatchSize_whenCollectedInBatches_thenBatchesOfEqualSizeAreProcessedCorrectly() {
        // Given
        List<Integer> allItems = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> batches = new ArrayList<>();
        List<Integer> aggregatedBatches = new ArrayList<>();

        // When
        List<Integer> result = allItems.stream().collect(batchCollector(2, batch -> {
            batches.add(new ArrayList<>(batch));
            aggregatedBatches.addAll(batch);
        }));

        // Then
        assertThat(batches.size(), equalTo(5));
        assertThat(batches.stream().allMatch(batch -> batch.size() == 2), is(true));
        assertThat(aggregatedBatches, equalTo(allItems));
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void givenAStreamOfItems_andBatchSizeEqualToTheTotalNumberOfItems_whenCollectedInBatches_thenOneBatchIsProcessedCorrectly() {
        // Given
        List<Integer> allItems = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> batches = new ArrayList<>();
        List<Integer> aggregatedBatches = new ArrayList<>();

        // When
        List<Integer> result = allItems.stream().collect(batchCollector(allItems.size(), batch -> {
            batches.add(new ArrayList<>(batch));
            aggregatedBatches.addAll(batch);
        }));

        // Then
        assertThat(batches.size(), equalTo(1));
        assertThat(batches.stream().flatMap(Collection::stream).collect(Collectors.toList()), equalTo(allItems));
        assertThat(aggregatedBatches, equalTo(allItems));
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void givenAStreamOfItems_andBatchSizeGreaterThanTheTotalNumberOfItems_whenCollectedInBatches_thenOneBatchIsProcessedCorrectly() {
        // Given
        List<Integer> allItems = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> batches = new ArrayList<>();
        List<Integer> aggregatedBatches = new ArrayList<>();

        // When
        List<Integer> result = allItems.stream().collect(batchCollector(allItems.size() + 999, batch -> {
            batches.add(new ArrayList<>(batch));
            aggregatedBatches.addAll(batch);
        }));

        // Then
        assertThat(batches.size(), equalTo(1));
        assertThat(batches.stream().flatMap(Collection::stream).collect(Collectors.toList()), equalTo(allItems));
        assertThat(aggregatedBatches, equalTo(allItems));
        assertThat(result, equalTo(Collections.emptyList()));
    }
}