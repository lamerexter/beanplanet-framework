package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoUtil;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utilities for resolving, loading and handling resources.
 */
public class ResourceUtil {
    public static URI toUri(final String uri) {
        return uri == null ? null : URI.create(uri);
    }

    public static URI toUri(final URL url) {
        try {
            return url == null ? null : url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isFileUrl(final URL url) {
        if (url == null) return false;

        String protocol = url.getProtocol();
        return "file".equals(protocol) || "vfsfile".equals(protocol) || "vfs".equals(protocol);
    }

    public static boolean isFileUrl(final String url) {
        try {
            return url != null && isFileUrl(new URL(url));
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isJarUrl(final URL url) {
        String protocol = url.getProtocol();
        return "jar".equals(protocol);
    }

    public static boolean isJarUrl(final String url) {
        try {
            return isJarUrl(new URL(url));
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isZipCompatibleUrl(final URL url) {
        String protocol = url.getProtocol();
        return isJarUrl(url) || "zip".equals(protocol) || "vfszip".equals(protocol) || "wsjar".equals(protocol);
    }

    public static boolean isZipCompatibleUrl(final String url) {
        try {
            return isZipCompatibleUrl(new URL(url));
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static FileResource toFileResource(final URL url) {
        if (url == null) return null;
        if ( isFileUrl(url) ) return new FileResource(toUri(url).getSchemeSpecificPart());

        final FileResource tempFileResource = new FileResource(FileUtil.createTemporaryFile());
        IoUtil.transferResourceStreamsAndClose(new UrlResource(url), tempFileResource);
        return tempFileResource;
    }

    public static FileResource toFileResource(final String url) {
        try {
            return toFileResource(new URL(url));
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
}
