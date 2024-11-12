package org.beanplanet.core.net.http.handler;

import org.beanplanet.core.net.http.converter.Jackson2HttpMessageBodyConverter;
import org.beanplanet.core.net.http.converter.SystemHttpMessageBodyConverterRegistry;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SystemHttpMessageBodyConverterRegistryTest {
    @Test
    public void givenHandlersExist_whenTheSystemRegistryIsLoaded_thenAllHandlersAreLoadedSuccessfully() {
        assertThat(SystemHttpMessageBodyConverterRegistry.getInstance().size(), not(equalTo(0)));
        assertThat(SystemHttpMessageBodyConverterRegistry.getInstance().findEntriesOfType(Jackson2HttpMessageBodyConverter.class).count(), equalTo(1L));
    }
}