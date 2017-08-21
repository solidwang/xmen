/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.registry.RegistryFactory;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.Protocol;
import com.solid4j.xmen.rpc.ProxyFactory;

/**
 * @author solidwang
 * @since 1.0
 */
public class RefererConfig<T> extends AbstractInterfaceConfig {

    private static final Protocol refprotocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();

    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

    private static RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getDefaultExtension();

    // 服务接口
    private Class<?> interfaceClass;

    // 接口代理类引用
    private transient volatile T ref;
    private transient volatile Invoker<?> invoker;

    protected synchronized T getRef() {
        if (ref == null) {
            init();
        }
        return ref;
    }

    private void init() {
        ref = createProxy();
    }

    @SuppressWarnings("unchecked")
    private T createProxy() {
        String registryURL = registryFactory.discovery(interfaceClass.getName());
        URL url = URL.valueOf(registryURL);
        invoker = refprotocol.refer(interfaceClass, url);
        T t = (T) proxyFactory.getProxy(invoker);
        return t;
    }

    public Class<?> getInterface() {

        return interfaceClass;
    }

    public void setInterface(Class<?> interfaceClass) {

        this.interfaceClass = interfaceClass;
    }
}
