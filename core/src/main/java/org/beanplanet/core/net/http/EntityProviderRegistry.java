package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.beanplanet.core.util.CollectionUtil.nullSafe;

/**
 * An entity factory which delegates to registered factories by content type.
 *
 * @author Gary Watson
 */
public class EntityProviderRegistry implements EntityProvider {
    private List<EntityProvider> entityProviders = new ArrayList<>();

    public EntityProviderRegistry addProvider(EntityProvider provider) {
        entityProviders.add(provider);
        return this;
    }

    /**
     * Determines whether the provider can produce an entity for the given HTTP Message, content type and object.
     *
     * @param message the HTTP message for which the entity is to be created.
     * @return false if this provider cannot produce an entity of the specified content type for the given object or true if the
     * provider may be able to produce an entity. If true, a subsequent call to {@link #createEntity(HttpMessage, Resource)} )} may be
     * made to attempt to produce an entity.
     */
    @Override
    public boolean canCreateEntity(HttpMessage message) {
        for (EntityProvider provider : nullSafe(entityProviders)) {
            if ( provider.canCreateEntity(message) ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public HttpEntity createEntity(HttpMessage message, Resource content) {
        for (EntityProvider provider : nullSafe(entityProviders)) {
            if ( provider.canCreateEntity(message) ) {
                HttpEntity entity = provider.createEntity(message, content);
                if (entity != null) return entity;
            }
        }

        return null;
    }
//
//    @Override
//    public boolean canCreateObjectForEntity(HttpMessage message, HttpEntity entity, Class<?> clazz) {
//        for (EntityProvider provider : nullSafe(entityProviders)) {
//            if ( provider.canCreateObjectForEntity(message, entity, clazz) ) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public <T> T createObjectForEntity(HttpMessage message, HttpEntity entity, Class<T> clazz) {
//        for (EntityProvider provider : nullSafe(entityProviders)) {
//            if ( provider.canCreateObjectForEntity(message, entity, clazz) ) {
//                T object = provider.createObjectForEntity(message, entity, clazz);
//                if (object != null) return object;
//            }
//        }
//
//        return null;
//    }
}