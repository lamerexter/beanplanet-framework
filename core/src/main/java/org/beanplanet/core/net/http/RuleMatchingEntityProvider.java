package org.beanplanet.core.net.http;

import java.util.function.Predicate;

/**
 * A rule matching entity provider. The rule executes in context of an HTTP message (request/response).
 *
 * @author Gary Watson
 */
public interface RuleMatchingEntityProvider extends EntityProvider {
    Predicate<HttpMessage> getMatcher();
}