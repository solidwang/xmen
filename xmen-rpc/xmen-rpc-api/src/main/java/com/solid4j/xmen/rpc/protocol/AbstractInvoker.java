/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.protocol;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.rpc.Invoker;

/**
 * @author solidwang
 * @since 1.0
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {

    protected URL url;

    private Class<T> type;

    public AbstractInvoker(Class<T> type, URL url) {
        this.type = type;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public RpcResponse invoke(RpcRequest request) {
        return doInvoke(request);
    }

    @Override
    public URL getURL() {
        return url;
    }

    protected abstract RpcResponse doInvoke(RpcRequest request);
}
