/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.proxy.jdk;

import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.proxy.AbstractProxyFactory;
import com.solid4j.xmen.rpc.proxy.AbstractProxyInvoker;
import com.solid4j.xmen.rpc.proxy.InvokerInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author solidwang
 * @since 1.0
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        Class<?> interfaceClass = invoker.getInterface();
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
                new InvokerInvocationHandler(invoker));
    }

}
