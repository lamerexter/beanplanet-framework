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

package org.beanplanet.csv;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The Class CSVReader.
 */
public class CsvReader {

    /**
     * Instantiates a new cSV reader.
     */
    public CsvReader() {
    }

    public Stream<List<CharSequence>> streamLines(Resource resource) {
        return streamLines(resource, "UTF-8");
    }

    public Stream<List<CharSequence>> streamLines(Resource resource, String encoding) {
        Reader reader = resource.getReader(encoding);
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(lineIterator(reader), Spliterator.ORDERED), false)
                .onClose(() -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new IoException(e);
                    }
                });
    }

    /**
     * Record iterator.
     *
     * @param reader
     *           the reader
     * @return the iterator
     */
    private static final Iterator<List<CharSequence>> lineIterator(final Reader reader) {
        return new Iterator<List<CharSequence>>() {
            private boolean first = true;
            private CSVResourceParser parser;

            private List<CharSequence> nextRecord;

            public boolean hasNext() {
                if (first) {
                    parser = new CSVResourceParser(reader);
                    first = false;
                }

                try {
                    List<CharSequence> fields = parser.Line();
                    if ( fields == null ) {
                        nextRecord = null;
                        return false;
                    }

                    nextRecord = fields;
                    return true;
                } catch (ParseException parseEx) {
                    throw new RuntimeException(parseEx);
                }
            }

            public List<CharSequence> next() {
                if (nextRecord == null) {
                    throw new IllegalStateException("There is no record to return through this iterator.  Did you call hasNext() first?");
                }
                return nextRecord;
            }

            public void remove() {
                throw new UnsupportedOperationException("Removal of records through this iterator is unsupported.");
            }
        };
    }

    public static void main(String args[]) throws Exception {
        new CsvReader().streamLines(new StringResource("l1c1,l1c2,l1c3\nl2c1,l2c2,l2c3\r\n"))
                .forEach(System.out::println);
    }
}
