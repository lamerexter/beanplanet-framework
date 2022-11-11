package org.beanplanet.core.beans;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BeanUtilTest {
    @Test
    public void copyProperties_successful() {
        // Given
        TestBean source = new TestBean("theStringProperty", 1234);
        TestBean target = new TestBean();

        // When
        BeanUtil.copyProperties(source, target);

        //Then
        assertThat(target, equalTo(source));
    }
}