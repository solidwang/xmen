/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import com.solid4j.xmen.config.spring.handler.XmenNamespaceHandler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * service配置解析
 *
 * @author solidwang
 * @since 1.0
 */
public class ServiceConfigBean<T> extends ServiceConfig<T>
        implements BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfigBean.class);

    private transient BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化配置文件：注册中心和注册协议
        configRegistry();
        configProtocol();
    }

    private void configProtocol() {
        List<ProtocolConfig> protocols = new ArrayList<ProtocolConfig>();
        for (String name : XmenNamespaceHandler.protocolDefineNames) {
            ProtocolConfig protocol = beanFactory.getBean(name, ProtocolConfig.class);
            protocols.add(protocol);
        }
        setProtocols(protocols);
    }

    private void configRegistry() {
        for (String name : XmenNamespaceHandler.registryDefineNames) {
            RegistryConfig registoryConfig = beanFactory.getBean(name, RegistryConfig.class);
            setRegistry(registoryConfig);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        String serviceName = interfaceClass.getName();
        if (!"".equals(serviceName) && serviceName.length() != 0)
            handlerMap.putIfAbsent(serviceName, ref);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 发布服务
        if (getExported().get()) {
            LOGGER.info("service is exported");
            return;
        }
        export();
    }

    @Override
    public void destroy() throws Exception {
        // 销毁服务
        LOGGER.debug("destroy service");
    }

}
