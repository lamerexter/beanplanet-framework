package org.beanplanet.core.net.http.handler;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SystemHttpMessageBodyHandlerRegistryTest {
    @Test
    public void givenHandlersExist_whenTheSystemRegistryIsLoaded_thenAllHandlersAreLoadedSuccessfully() {
        assertThat(SystemHttpMessageBodyHandlerRegistry.getInstance().size(), not(equalTo(0)));
        assertThat(SystemHttpMessageBodyHandlerRegistry.getInstance().findEntriesOfType(Jackson2HttpMessageBodyHandler.class).count(), equalTo(1L));
    }
}