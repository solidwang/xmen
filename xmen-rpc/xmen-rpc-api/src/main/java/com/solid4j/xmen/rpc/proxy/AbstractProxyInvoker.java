/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.proxy;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.rpc.Invoker;

/**
 * @author solidwang
 * @since 1.0
 */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private T proxy;

    private Class<T> type;

    private URL url;

    public AbstractProxyInvoker(T poxy, Class<T> type, URL url) {
        this.proxy = proxy;
        this.type = type;
        this.url = url;
    }

    @Override
    public URL getURL() {
        return url;

    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public RpcResponse invoke(RpcRequest request) {

        return null;
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;
}
