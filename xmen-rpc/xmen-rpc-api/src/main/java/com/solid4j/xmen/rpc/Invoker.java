/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.common.rpc.Node;

/**
 * @author solidwang
 * @since 1.0
 */
public interface Invoker<T> extends Node {

    Class<T> getInterface();
    
    RpcResponse invoke(RpcRequest request);
}

    