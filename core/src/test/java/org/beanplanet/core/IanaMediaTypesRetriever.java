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

package org.beanplanet.core;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Utility to retrieve the media types from the IANA website, used as a basis to create the iana-media-types.properties
 * registry.
 */
public class IanaMediaTypesRetriever implements Runnable, Logger {
    private static final String IANA_MEDIA_TYPES_BASE_URL = "https://www.iana.org/assignments/media-types/%s.csv";
    private static final String MEDIA_TYPES_URL_SUFFIXES[] = new String [] {
            "application",
            "audio",
            "font",
            "image",
            "message",
            "model",
            "multipart",
            "text",
            "video"
    };

    public static void main(String ... args) {
        new IanaMediaTypesRetriever().run();
    }

    @Override
    public void run() {
        String mediaBaseType[] = { "" };
        int totalProcessed[] = {0};
        File mediaTypesFile = new File(FileUtil.getTemporaryFilesDirectory(), "iana-iana-media-types.properties");
        try (FileWriter fw = new FileWriter(mediaTypesFile)) {
            Arrays.stream(MEDIA_TYPES_URL_SUFFIXES).map(s -> String.format(IANA_MEDIA_TYPES_BASE_URL, s)).forEach(mediaTypeUrl -> {
                int processed[] = {0};
                info("Retrieving IANA media types from: {0}", mediaTypeUrl);
                try (BufferedReader reader = new BufferedReader(new UrlResource(mediaTypeUrl).getReader())) {
                    final boolean isHeader[] = {true};
                    final String lastTypeBaseType[] = {""};
                    reader.lines().map(l -> l.split(",")).filter(csv -> csv.length >= 3).forEach(csv -> {
                        if (isHeader[0]) {
                            isHeader[0] = false;
                            return;
                        }

                        try {
                            if (!StringUtil.isEmptyOrNull(csv[1])) {
                                fw.write(csv[1] + "=" + csv[1] + System.lineSeparator());
                                lastTypeBaseType[0] = new SimpleMediaType(csv[1]).getBaseType();
                            } else {
                                fw.write(lastTypeBaseType[0] + "/" + csv[0] + "=" + lastTypeBaseType[0] + "/" + csv[0] + System.lineSeparator());
                            }
                            processed[0]++;
                            totalProcessed[0]++;
                        } catch (IOException e) {
                            throw new IoException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new IoException(e);
                }
                info(String.format("Processed %,d media types from: %s", processed[0], mediaTypeUrl));
            });

            info(String.format("Processed a total of %,d media types from and saved as %s", totalProcessed[0], mediaTypesFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new IoException(e);
        }
    }
}
