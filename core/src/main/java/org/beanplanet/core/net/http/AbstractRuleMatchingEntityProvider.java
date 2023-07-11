package org.beanplanet.core.net.http;

import org.beanplanet.core.Predicates;

import java.util.function.Predicate;

/**
 * The base of an entity provider which can produce content based on a matching predicate or rule.
 *
 * @author Gary Watson
 */
public abstract class AbstractRuleMatchingEntityProvider implements RuleMatchingEntityProvider {
    private final Predicate<HttpMessage> matcher;

    public AbstractRuleMatchingEntityProvider() {
        this(Predicates.falsePredicate());
    }

    public AbstractRuleMatchingEntityProvider(Predicate<HttpMessage> matcher) {
        this.matcher = matcher;
    }

    /**
     * Determines whether the provider can produce an entity for the given HTTP Message, content type and object.
     *
     * @param message the HTTP message for which the entity is to be created.
     * @return false if this provider cannot produce an entity of the specified content type for the given object or true if the
     * provider may be able to produce an entity. If true, a subsequent call to {@link #createEntity(HttpMessage)} may be
     * made to attempt to produce an entity.
     */
    public boolean canCreateEntity(HttpMessage message) {
        return matcher.test(message);
    }

//    @Override
//    public boolean canCreateObjectForEntity(HttpMessage message, HttpEntity entity, Class<?> clazz) {
//        return matcher.test(message);
//    }
//
    @Override
    public Predicate<HttpMessage> getMatcher() {
        return matcher;
    }
}