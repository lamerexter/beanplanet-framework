/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.validation;

import org.springframework.beans.BeanWrapperImpl;

import java.util.function.Function;

public class SpringBeanValueProvider<T, R> implements Function<T, R> {
    private String script;

    public SpringBeanValueProvider(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R apply(T t) {
        if (t == null) return null;

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(t);
        return (R)beanWrapper.getPropertyValue(script);
    }
}
