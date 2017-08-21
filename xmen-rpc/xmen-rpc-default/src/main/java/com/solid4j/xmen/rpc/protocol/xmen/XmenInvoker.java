/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.protocol.xmen;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.remote.netty.NettyClient;
import com.solid4j.xmen.rpc.protocol.AbstractInvoker;

/**
 * @author solidwang
 * @since 1.0
 */
public class XmenInvoker<T> extends AbstractInvoker<T> {

    public XmenInvoker(Class<T> type, URL url) {
        super(type, url);
    }

    @Override
    protected RpcResponse doInvoke(RpcRequest request) {
        RpcResponse response = null;
        try {
            response = new NettyClient(url.getHost(), url.getPort()).send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
