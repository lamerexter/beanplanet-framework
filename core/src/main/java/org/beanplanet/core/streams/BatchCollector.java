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

import org.beanplanet.core.lang.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * <p>
 * A collector of {@link java.util.stream.Stream} elements into batches of a given size, calling a configured batch
 * processor for each batch of the given size or for the last batch.
 * </p>
 * <p>
 * Parallel streams are supported provided the batch processor can handle batches of items less than the given size and
 * in any order (i.e. it can organise unordered batches itself).
 * </p>
 * <p>
 * This is a fairly efficient implementation where elements are not kept in memory, save for the configured batch size
 * of items (and parallelism, if required).
 * </p>
 * <p>
 * The final result of the collection is an empty list, with all streamed items being batched-up and batch processor
 * being called to process each.
 * </p>
 */
public class BatchCollector<T> implements Collector<T, List<T>, List<T>> {

    private final int batchSize;
    private final Consumer<List<T>> batchProcessor;

    /**
     * Constructs a batch collector which collects elements in batches of the given size, calling the
     * given batch processor for each batch.
     *
     * @param batchSize the number of items batched together and then passed to the batch processor.
     * @param batchProcessor a processor of batches of the given size or less.
     */
    public BatchCollector(int batchSize, Consumer<List<T>> batchProcessor) {
        Assert.notNull(batchProcessor, "A batch processor must be specified");

        this.batchSize = batchSize;
        this.batchProcessor = batchProcessor;
    }

    /**
     * Combines two partial batches, which in this implementation simply dispatches the batches to the batch processor.
     *
     * @return a combiner of partial batches.
     */
    public BinaryOperator<List<T>> combiner() {
        return (batch1, batch2) -> {
            // In the case of parallel stream processing we choose to process each parallel batch immediately, without
            // checking each against the batch size, for simplicity and to avoid having to deal with possible remainders.
            batchProcessor.accept(batch1);
            batchProcessor.accept(batch2);
            return Collections.emptyList();
        };
    }

    /**
     * Supplies the collector with the collection implementation.
     *
     * @return a new list for collecting items in batches.
     */
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    /**
     * The accumulator of batched items into the mutable batch collection (list). Once the configured items have been
     * accumulated, hands-off the items to the batch processor.
     *
     * @return an accumulator/dispatcher of items.
     */
    public BiConsumer<List<T>, T> accumulator() {
        return (batch, item) -> {
            batch.add(item);
            if (batch.size() >= batchSize) {
                batchProcessor.accept(batch);
                batch.clear();
            }
        };
    }

    /**
     * Completes the collection operation by checking if there are residual items left and, if so, dispatches them
     * to the batch processor.
     *
     * @return a completion function which checks for remaining items to be batch processed.
     */
    public Function<List<T>, List<T>> finisher() {
        return ts -> {
            if (!ts.isEmpty()) {
                batchProcessor.accept(ts);
            }
            return Collections.emptyList();
        };
    }

    /**
     * The characteristics of the stream collection. In this implementation nothing special.
     *
     * @return an empty set of characteristics.
     */
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
