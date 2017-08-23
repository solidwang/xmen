/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import com.solid4j.xmen.common.constant.RegConstant;
import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.registry.RegistryFactory;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.Protocol;
import com.solid4j.xmen.rpc.ProxyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * referer配置解析
 *
 * @author solidwang
 * @since 1.0
 */
public class RefererConfig<T> extends AbstractInterfaceConfig {

    // referer协议
    private static final Protocol refprotocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    // 代理工厂
    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
    // 注册工厂
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
        // 注册中心URL
        RegistryConfig registryConfig = getRegistry();
        URL regURL = getRegURL(registryConfig);

        // 获取服务URL
        String serviceURL = registryFactory.discovery(interfaceClass.getName(), regURL);
        URL url = URL.valueOf(serviceURL);
        invoker = refprotocol.refer(interfaceClass, url);
        T t = (T) proxyFactory.getProxy(invoker);
        return t;
    }

    private URL getRegURL(RegistryConfig registryConfig) {
        String protocol = registryConfig.getRegProtocol();
        String host = registryConfig.getAddress();
        Integer connectTimeout = registryConfig.getConnectTimeout();
        Map<String, String> paramaters = new HashMap<>();
        paramaters.put(RegConstant.CONNECT_TIMEOUT, String.valueOf(connectTimeout));
        URL regURL = new URL(protocol, host, 0, paramaters);
        return regURL;
    }

    public Class<?> getInterface() {
        return interfaceClass;
    }

    public void setInterface(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
