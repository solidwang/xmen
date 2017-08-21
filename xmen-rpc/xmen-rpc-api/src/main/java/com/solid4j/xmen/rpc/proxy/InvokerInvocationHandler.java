/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.proxy;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.rpc.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author solidwang
 * @since 1.0
 */
public class InvokerInvocationHandler implements InvocationHandler {

    private final Invoker<?> invoker;

    public InvokerInvocationHandler(Invoker<?> handler) {
        this.invoker = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        RpcRequest request = new RpcRequest();
        request.setInterfaceName(invoker.getInterface().getName());
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setRequestId(System.currentTimeMillis() + "");
        request.setParameters(args);

        return invoker.invoke(request).getResult();
    }

}
