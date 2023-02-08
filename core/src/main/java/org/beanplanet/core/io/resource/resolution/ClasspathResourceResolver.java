package org.beanplanet.core.io.resource.resolution;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UrlResource;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.beanplanet.core.util.EnumerationUtil.toList;

public class ClasspathResourceResolver implements ResourceResolver {
    private ClassLoader classLoader;
    private Class<?> relativeToClass;

    public ClasspathResourceResolver() {}

    public ClasspathResourceResolver(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClasspathResourceResolver(final Class<?> relativeToClass) {
        this.relativeToClass = relativeToClass;
    }

    /**
     * Attempts to resolve one or more resources from the given descriptor. If the descriptor contains wildcards, resolving to multiple resources, all of the resolved resources will be returned.
     *
     * @param descriptor a descriptor of the resource(s) being sought.
     * @return the resources matching the given descriptor, or empty (never <code>null</code>) if none were found.
     */
    @Override
    public Resource[] resolveResources(final String descriptor) {
        Resource[] foundResources = null;
        ClassLoader cl = classLoader;
        if (cl != null) foundResources = resolveThroughClassloader(cl, descriptor);
        if (foundResources != null) return foundResources;

        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) foundResources = resolveThroughClassloader(cl, descriptor);
        if (foundResources != null) return foundResources;

        cl = getClass().getClassLoader();
        if (cl != null) foundResources = resolveThroughClassloader(cl, descriptor);
        if (foundResources != null) return foundResources;

        Class<?> clazz = relativeToClass;
        if (clazz != null) foundResources = resolveRelativeToClass(clazz, descriptor);
        if (foundResources != null) return foundResources;

        clazz = getClass();
        if (clazz != null) foundResources = resolveRelativeToClass(clazz, descriptor);
        if (foundResources != null) return foundResources;

        return EMPTY_RESOURCES;
    }

    private Resource[] resolveThroughClassloader(final ClassLoader classLoader, final String descriptor) {
        try {
            final List<UrlResource> resourcesFound = toList(classLoader.getResources(descriptor)).stream().map(UrlResource::new).collect(Collectors.toList());
            return resourcesFound.isEmpty() ? null : resourcesFound.toArray(new UrlResource[resourcesFound.size()]);
        } catch (IOException e) {
            return null;
        }
    }

    private Resource[] resolveRelativeToClass(final Class<?> clazz, final String descriptor) {
        URL foundResource = clazz.getResource(descriptor);
        return foundResource != null ? new Resource[] { new UrlResource(foundResource) } : null;
    }
}
