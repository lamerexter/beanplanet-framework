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

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;

/**
 * Utility class containing stream related functions.
 */
public class StreamUtil {
    public static Collector<Boolean, ?, boolean[]> toBooleanArray() {
        class PrimitiveBooleanAccumulator {
            BitSet bs = new BitSet();
            int size = 0;
        }

        return Collector.of(PrimitiveBooleanAccumulator::new,
                (acc, t) -> acc.bs.set(acc.size++, t),
                (acc1, acc2) -> {
                    acc2.bs.stream().forEach(i -> acc1.bs.set(i + acc1.size));
                    acc1.size += acc2.size;
                    return acc1;
                }, acc -> {
                    boolean[] res = new boolean[acc.size];
                    acc.bs.stream().forEach(i -> res[i] = true);
                    return res;
                });
    }

    public static Collector<Byte, ?, byte[]> toByteArray() {
        class PrimitiveByteAccumulator {
            List<Byte> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveByteAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    byte[] res = new byte[acc.collection.size()];
                    int i = 0;
                    for (Byte source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Character, ?, char[]> toCharArray() {
        class PrimitiveByteAccumulator {
            List<Character> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveByteAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    char[] res = new char[acc.collection.size()];
                    int i = 0;
                    for (Character source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Double, ?, double[]> toDoubleArray() {
        class PrimitiveDoubleAccumulator {
            List<Double> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveDoubleAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    double[] res = new double[acc.collection.size()];
                    int i = 0;
                    for (Double source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Float, ?, float[]> toFloatArray() {
        class PrimitiveFloatAccumulator {
            List<Float> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveFloatAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    float[] res = new float[acc.collection.size()];
                    int i = 0;
                    for (Float source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Integer, ?, int[]> toIntArray() {
        class PrimitiveIntegerAccumulator {
            List<Integer> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveIntegerAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    int[] res = new int[acc.collection.size()];
                    int i = 0;
                    for (Integer source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Long, ?, long[]> toLongArray() {
        class PrimitiveLongAccumulator {
            List<Long> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveLongAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    long[] res = new long[acc.collection.size()];
                    int i = 0;
                    for (Long source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    public static Collector<Short, ?, short[]> toShortArray() {
        class PrimitiveShortAccumulator {
            List<Short> collection = new LinkedList<>();
        }

        return Collector.of(PrimitiveShortAccumulator::new,
                (acc, t) -> acc.collection.add(t),
                (acc1, acc2) -> {
                    acc1.collection.addAll(acc2.collection);
                    return acc1;
                },
                acc -> {
                    short[] res = new short[acc.collection.size()];
                    int i = 0;
                    for (Short source : acc.collection) {
                        res[i++] = source;
                    }
                    return res;
                });
    }

    /**
     * Creates a batch collector of items which collects and then dispatches items in batches of a given batch size.
     *
     * @param batchSize      the maximum number of elements to collect in a batch before dispatching through the given batch processor.
     * @param batchProcessor a processor of batched items.
     * @param <T>            the type if items collected and processed in batches.
     * @return a new batch collector.
     */
    public static <T> BatchCollector<T> batchCollector(int batchSize, Consumer<List<T>> batchProcessor) {
        return new BatchCollector<>(batchSize, batchProcessor);
    }
}
