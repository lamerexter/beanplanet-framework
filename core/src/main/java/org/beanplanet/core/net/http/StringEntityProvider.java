package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

/**
 * A factory for creating text-based HTTP entity bodies.
 *
 * @author Gary Watson
 */
public class StringEntityProvider extends AbstractRuleMatchingEntityProvider {
    public StringEntityProvider() {
        super(EntityMatchers.mediaTypes("text/.*"));
    }

    @Override
    public HttpEntity createEntity(HttpMessage message, Resource content) {
        return new StringEntity(content, ContentType.from(message));
    }
//
//    public <T> T createObject(HttpMessage message, HttpEntity entity, Class<T> clazz) {
//        if ( !CharSequence.class.isAssignableFrom(clazz) ) return null;
//
//        return (T)entity.getContent().readFullyAsString(entity.getCharset());
//    }
}