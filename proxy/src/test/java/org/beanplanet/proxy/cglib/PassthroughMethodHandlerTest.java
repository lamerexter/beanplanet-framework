/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.proxy.cglib;

import org.beanplanet.core.lang.proxy.DefaultProxyFactory;
import org.beanplanet.core.lang.proxy.PassthroughMethodHandler;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

@Ignore("Should run in perf tests")
public class PassthroughMethodHandlerTest {

    @Rule
    public final ContiPerfRule rule = new ContiPerfRule();

    public interface RandomNumberGenerator {
        long getNumber();
    }

    public static class ConcreteRandomNumberGenerator implements RandomNumberGenerator {
        private Random random = new Random();

//        public ConcreteRandomNumberGenerator() {}

        @Override
        public long getNumber() {
            return random.nextLong();
        }
    }

    private static final Random random = new Random();
    private static final RandomNumberGenerator concreteInstance = random::nextLong;
    private static final RandomNumberGenerator jdkProxiedInstance = jdkPassthroughProxy(concreteInstance);
    private static final RandomNumberGenerator cglibProxiedInstance = cglibPassthroughProxy(concreteInstance);
    private static final RandomNumberGenerator cglibSuperclassProxiedInstance = cglibSuperclassProxy();

    @Test
    @PerfTest(invocations = 1000, warmUp = 200)
    public void invokeConcrete() {
        getAMillionRandomLongs(concreteInstance);
    }

    @Test
    @PerfTest(invocations = 1000, warmUp = 200)
    public void invokeJdkProxied() {
        getAMillionRandomLongs(jdkProxiedInstance);
    }

    @Test
    @PerfTest(invocations = 1000, warmUp = 200)
    public void invokeCglibProxied() {
        getAMillionRandomLongs(cglibProxiedInstance);
    }

    @Test
    @PerfTest(invocations = 1000, warmUp = 200)
    public void invokeCglibSuperclassProxied() {
        getAMillionRandomLongs(cglibSuperclassProxiedInstance);
    }

    private void getAMillionRandomLongs(RandomNumberGenerator generator) {
        for (int i = 0; i < 1000000; i++) {
            generator.getNumber();
        }
    }

    private static RandomNumberGenerator jdkPassthroughProxy(RandomNumberGenerator wrappedinstance) {
        return DefaultProxyFactory.getInstance().dynamicProxy(RandomNumberGenerator.class, new PassthroughMethodHandler<>(wrappedinstance));
    }

    private static RandomNumberGenerator cglibPassthroughProxy(RandomNumberGenerator wrappedinstance) {
        return new CGLibProxyFactory().dynamicProxy(RandomNumberGenerator.class, new PassthroughMethodHandler<>(wrappedinstance));
    }

    private static RandomNumberGenerator cglibSuperclassProxy() {
        return new CGLibProxyFactory().dynamicProxy(ConcreteRandomNumberGenerator.class, context -> ((CGLibMethodCallContext)context).getMethodProxy().invokeSuper(context.getTarget(), context.getParameters()));
    }
}