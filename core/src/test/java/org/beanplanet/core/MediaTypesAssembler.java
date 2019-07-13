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
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.util.MultiValueListMapImpl;
import org.beanplanet.core.util.MultiValueMap;
import org.beanplanet.core.util.MultiValueMapImpl;
import org.beanplanet.core.util.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.beanplanet.core.util.CollectionUtil.isEmptyOrNull;
import static org.beanplanet.core.util.ObjectUtil.nvl;

/**
 * Assembles known IANA Media Types and derived information into the META-INF/services registry.
 */
public class MediaTypesAssembler implements Runnable, Logger {
    public static void main(String ... args) {
        new MediaTypesAssembler().run();
    }

    @Override
    public void run() {
        File mediaTypesFile = new File(FileUtil.getTemporaryFilesDirectory(), "media-types.properties");
        try (FileWriter fw = new FileWriter(mediaTypesFile)){
            Properties mediaTypes = new Properties();
            mediaTypes.load(getClass().getResource("iana-media-types.properties").openStream());
            Properties mediaTypeDescriptions = new Properties();
            mediaTypeDescriptions.load(getClass().getResource("media-type-descriptions.properties").openStream());

            //----------------------------------------------------------------------------------------------------------
            // Map file extensions to known media types and back.
            //----------------------------------------------------------------------------------------------------------
            Properties fileExtMediaTypes = new Properties();
            fileExtMediaTypes.load(getClass().getResource("file-extension-media-types.properties").openStream());
            MultiValueMapImpl<String, String> fileExtensionsToMediaTypes = new MultiValueMapImpl<>();
            fileExtMediaTypes.forEach((k, v) -> {
                fileExtensionsToMediaTypes.addAllValues(k.toString(), StringUtil.asCsvList(v.toString()));
            });
            MultiValueListMapImpl<String, String > mediaTypeFileExtensions = new MultiValueListMapImpl<>();
            fileExtMediaTypes.forEach((k, v) -> {
                List<String> mediaTypesForExtension = StringUtil.asCsvList(v.toString());
                mediaTypesForExtension.forEach(mt -> mediaTypeFileExtensions.addValue(mt, k.toString()));

            });

            for (Object m : new TreeSet<>(mediaTypes.keySet())) {
                String mediaTypeName = m.toString();
                SimpleMediaType mediaType = new SimpleMediaType(mediaTypeName);

                List<String> fileExtensionsForMediaType = mediaTypeFileExtensions.get(mediaTypeName);
                if ( fileExtensionsForMediaType != null ) {
                    mediaType.setFileExtensions(fileExtensionsForMediaType);
                }
                String description = (String) mediaTypeDescriptions.get(mediaTypeName);
                fw.write(String.format("%s=%s|%s\n", mediaType.getName(), nvl(description, ""), isEmptyOrNull(mediaType.getFileExtensions()) ? "" : StringUtil.asDelimitedString(mediaType.getFileExtensions(), ",")));
            }
            info(String.format("Saved %,d media types to %s", mediaTypes.size(), mediaTypesFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new IoException(e);
        }

    }
}
