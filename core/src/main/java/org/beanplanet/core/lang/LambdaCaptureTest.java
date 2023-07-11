package org.beanplanet.core.lang;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class LambdaCaptureTest {
    public static Stream<String> doIt() {
        String prefix = "W5";
        List<String> postCodes = asList("W5 4LT", "DL1 2BG");
        return postCodes
                .stream()
                .filter(postCode -> postCode.contains(prefix));
    }
}
