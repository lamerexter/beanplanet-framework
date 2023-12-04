package org.beanplanet.core.mediatypes;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.resource.Resource;

import java.util.Collections;
import java.util.List;

/**
 * Utility for accessing media type information and functionality.
 */
public class MediaTypeUtil {
    public static List<MediaType> determineMediaTypesForResource(final String resourceSpec) {
        final String filenameExtension = FileUtil.getFilenameSuffix(resourceSpec);
        if (filenameExtension == null) return null;

        return SystemMediaTypesRegistry.getInstance().findMediaTypesForFileExtension(filenameExtension);
    }

    public static List<MediaType> determineMediaTypesForResource(final Resource resource) {
        final String resourceName = resource.getName();
        return resourceName == null ? Collections.emptyList() : determineMediaTypesForResource(resourceName);
    }
}
