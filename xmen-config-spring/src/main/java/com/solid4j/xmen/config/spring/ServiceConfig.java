/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import com.solid4j.xmen.common.constant.XmenConstant;
import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.common.util.NetUtil;
import com.solid4j.xmen.rpc.Exporter;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.Protocol;
import com.solid4j.xmen.rpc.ProxyFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * service配置解析
 *
 * @author solidwang
 * @since 1.0
 */
public class ServiceConfig<T> extends AbstractInterfaceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);
    // 代理工厂类
    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
    // 发布协议
    private static final Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    // 判断service是否已经发布
    private static AtomicBoolean exported = new AtomicBoolean(false);
    // Map(ServiceName ==> Instance)
    protected static final ConcurrentMap<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    // 服务接口
    protected Class<?> interfaceClass;
    // 服务实现
    protected T ref;

    public Class<?> getInterface() {

        return interfaceClass;
    }

    public void setInterface(Class<?> interfaceClass) {

        this.interfaceClass = interfaceClass;
    }

    public T getRef() {

        return ref;
    }

    public void setRef(T ref) {

        this.ref = ref;
    }

    public synchronized void export() {
        // 循环执行多协议export
        LOGGER.info("service export start...");
        List<RegistryConfig> registrys = getRegistries();
        List<ProtocolConfig> protocols = getProtocols();
        for (ProtocolConfig protocol : protocols) {
            doExport(protocol, registrys);
        }
    }

    private void doExport(ProtocolConfig protocolConfig, List<RegistryConfig> registrys) {
        String protocolName = protocolConfig.getName();
        Integer port = protocolConfig.getPort();
        if (protocolName == null || protocolName.length() == 0) {
            protocolName = XmenConstant.PROTOCOL_NAME;
        }
        //构建服务发布URL
        String host = NetUtil.getLocalAddress();
        URL url = new URL(protocolName, host, port);
        //遍历注册中心，进行服务发布
        for (RegistryConfig registryConfig : registrys) {
            Invoker<?> invoker = proxyFactory.getInvoker(ref, (Class<T>) interfaceClass, url);
            Exporter<T> exporter = protocol.export(invoker, handlerMap);
            exported.set(true);
        }
    }

    protected AtomicBoolean getExported() {
        return exported;
    }
}
