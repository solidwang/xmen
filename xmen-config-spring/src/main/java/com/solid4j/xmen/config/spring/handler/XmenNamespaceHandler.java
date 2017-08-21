/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring.handler;

import com.solid4j.xmen.config.spring.ProtocolConfig;
import com.solid4j.xmen.config.spring.RefererConfigBean;
import com.solid4j.xmen.config.spring.RegistryConfig;
import com.solid4j.xmen.config.spring.ServiceConfigBean;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author solidwang
 * @since 1.0
 */
public class XmenNamespaceHandler extends NamespaceHandlerSupport {

    //协议名称
    public final static Set<String> protocolDefineNames = new HashSet<String>();
    //注册中心名称
    public final static Set<String> registryDefineNames = new HashSet<String>();
    //服务名称
    public final static Set<String> serviceDefineName = new HashSet<String>();

    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new XmenBeanDefinitionParser(RegistryConfig.class, true));
        registerBeanDefinitionParser("protocol", new XmenBeanDefinitionParser(ProtocolConfig.class, true));
        registerBeanDefinitionParser("service", new XmenBeanDefinitionParser(ServiceConfigBean.class, true));
        registerBeanDefinitionParser("referer", new XmenBeanDefinitionParser(RefererConfigBean.class, false));
    }
}
