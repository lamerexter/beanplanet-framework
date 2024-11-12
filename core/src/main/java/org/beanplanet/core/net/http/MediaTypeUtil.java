package org.beanplanet.core.net.http;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.resource.Resource;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility for accessing media type information and functionality.
 */
public class MediaTypeUtil {
    private static volatile Random random;

    /**
     * <a href="https://www.rfc-editor.org/rfc/rfc2046#section-5.1.1">RFC 2046</a> set of known characters
     * suitable for robustness through mail/HTTP gateway transmission, in
     * <a href="https://www.asciitable.com/">ASCII table</a> order for easy reading.
     */
    private static final byte[] BOUNDARY_PARAM_CHARS =
            new byte[] {
                    '-',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                    'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                    't', 'u', 'v', 'w', 'x', 'y', 'z',
                    '_'
            };


    public static List<MediaType> determineMediaTypesForResource(final String resourceSpec) {
        final String filenameExtension = FileUtil.getFilenameSuffix(resourceSpec);
        if (filenameExtension == null) return null;

        return SystemMediaTypesRegistry.getInstance().findMediaTypesForFileExtension(filenameExtension);
    }

    public static List<MediaType> determineMediaTypesForResource(final Resource resource) {
        final String resourceName = resource.getName();
        return resourceName == null ? Collections.emptyList() : determineMediaTypesForResource(resourceName);
    }

    /**
     * As per <a href="https://www.rfc-editor.org/rfc/rfc2046#section-5.1.1">RFC 2046</a> generates a multipart
     * boundary delimiter.
     *
     * @return a delimiter suitable for separating multipart parts.
     */
    public static byte[] generateMultipartBoundary() {
        final Random randonGenerator = getOrCreateRandom();
        byte[] boundary = new byte[randonGenerator.nextInt(16) + 32];
        for (int i = 0; i < boundary.length; i++) {
            boundary[i] = BOUNDARY_PARAM_CHARS[randonGenerator.nextInt(BOUNDARY_PARAM_CHARS.length)];
        }
        return boundary;

    }

    private static Random getOrCreateRandom() {
        // This check is good, but may lead to multiple threads with the same answer
        // Either way, it does not matter if multiple mutate the random later...
        if (random == null) {
            synchronized (MediaTypeUtil.class) {
                // Just in case more than one thread was blocked waiting for mutex
                if (random == null) {
                    random = new SecureRandom();
                }
            }
        }

        return random;
    }
}
