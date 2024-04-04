package org.beanplanet.springcore.io;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpringResourceAdapterTest {
    @Test
    public void givenASpringReadableResource_whenAdaptedToABeanplanetResource_thenTheAdapterResourceSupportsAllFeaturesOfTheAdapted() {
        // Given
        final String resourceContent = "Hello World!";
        Resource adapted = new ByteArrayResource(resourceContent.getBytes());

        // When
        SpringResourceAdapter adapter = new SpringResourceAdapter(adapted);

        // Then
        assertThat(adapter.getAdaptedResource(), sameInstance(adapted));
        assertThat(adapter.readFullyAsString(), equalTo(resourceContent));
    }
}