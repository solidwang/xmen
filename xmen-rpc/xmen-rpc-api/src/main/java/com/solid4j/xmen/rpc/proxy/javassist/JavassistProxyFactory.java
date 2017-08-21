/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.proxy.javassist;

import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.proxy.AbstractProxyFactory;

/**
 * @author solidwang
 * @since 1.0
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        
        return null;
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        
        return null;
    }
}

    