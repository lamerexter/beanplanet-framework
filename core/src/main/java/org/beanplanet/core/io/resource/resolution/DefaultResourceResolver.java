package org.beanplanet.core.io.resource.resolution;

import org.beanplanet.core.io.resource.*;
import org.beanplanet.core.util.StringUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The default utility class for resolving URI, URL and classpath resources.
 */
public class DefaultResourceResolver implements ResourceResolver {
    private ClasspathResourceResolver classpathResourceResolver = new ClasspathResourceResolver();

    /**
     * Attempts to resolve a one or more resources from the given descriptor. If the descriptor contains wildcards, resolving to multiple resources, all of the resolved resources will be returned.
     *
     * @param descriptor a descriptor of the resource(s) being sought, which may not be null.
     * @return the resources matching the given descriptor, or empty (never <code>null</code>) if none were found.
     */
    @Override
    public Resource[] resolveResources(final String descriptor) {
        if ( isClasspathExplicitResource(descriptor) ) return new Resource[] { new ClasspathResource(StringUtil.lTrim(CLASSPATH_RESOURCE_PREFIX, descriptor)) };

        if ( isUrlResource(descriptor) ) return new Resource[] { new UrlResource(descriptor) };

        if ( isFileResource(descriptor) ) return new Resource[] { new FileResource(descriptor) };

        if ( isClasspathResource(descriptor) ) return classpathResourceResolver.resolveResources(descriptor);

        if ( isUriResource(descriptor) ) return new Resource[] { new UriResource(descriptor) };

        return EMPTY_RESOURCES;
    }

    protected boolean isClasspathExplicitResource(final String descriptor) {
        return descriptor.startsWith(CLASSPATH_RESOURCE_PREFIX);
    }

    protected boolean isUrlResource(final String descriptor) {
        try {
            new URL(descriptor);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    protected boolean isClasspathResource(final String descriptor) {
        return classpathResourceResolver.resolveResource(descriptor).isPresent();
    }

    protected boolean isFileResource(final String descriptor) {
        final File file = new File(descriptor);
        return file.exists();
    }

    protected boolean isUriResource(final String descriptor) {
        try {
            new URI(descriptor);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
