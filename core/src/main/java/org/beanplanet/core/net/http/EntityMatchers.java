package org.beanplanet.core.net.http;

import org.beanplanet.core.Predicates;
import org.beanplanet.core.mediatypes.MediaType;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * Useful HTTP entity matching functions.
 *
 * @author Gary Watson
 */
public class EntityMatchers {
    public static Predicate<HttpMessage> mediaTypes(MediaType ... mediaTypes) {
        return mediaTypes(asList(mediaTypes).stream().map(MediaType::getName).toArray(String[]::new));
    }

    public static Predicate<HttpMessage> mediaTypes(String ... mediaTypesRegEx) {
        if (mediaTypesRegEx == null) return Predicates.falsePredicate();

        final Pattern mimePatterns[] = new Pattern[mediaTypesRegEx.length];
        for (int n=0; n < mediaTypesRegEx.length; n++) {
            mimePatterns[n] = Pattern.compile(mediaTypesRegEx[n]);
        }

        return message -> {
            final String contentTypeHeader = message.getLastHeader(HttpHeaders.CONTENT_TYPE);
            if (contentTypeHeader == null) return false;

            for (int n=0; n < mimePatterns.length; n++) {
                if (mimePatterns[n].matcher(contentTypeHeader).matches()) {
                    return true;
                }
            }

            return false;
        };
    }
}

