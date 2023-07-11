package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

/**
 * A factory for creating HTTP entity bodies using the Jackson JSON library.
 *
 * @author Gary Watson
 */
public class ArbitraryDataEntityProvider extends AbstractRuleMatchingEntityProvider {
    public ArbitraryDataEntityProvider() {
        this("text/.*");
    }

    public ArbitraryDataEntityProvider(final String ... mediaTypePatterns) {
        super(EntityMatchers.mediaTypes(mediaTypePatterns));
    }

    @Override
    public HttpEntity createEntity(HttpMessage message, Resource content) {
        return new ArbitraryDataEntity(content, ContentType.from(message));
    }
//
//    public <T> T createObject(HttpMessage message, HttpEntity entity, Class<T> clazz) {
//        if ( !CharSequence.class.isAssignableFrom(clazz) ) return null;
//
//        return (T)entity.getContent().readFullyAsString(entity.getCharset());
//    }
}