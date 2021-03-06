/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import com.solid4j.xmen.config.spring.handler.XmenNamespaceHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * referer配置解析
 *
 * @author solidwang
 * @since 1.0
 */
public class RefererConfigBean<T> extends RefererConfig<T> implements FactoryBean<T>, BeanFactoryAware, ApplicationContextAware, InitializingBean {

    private transient BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        configRegistry();
    }

    private void configRegistry() {
        for (String name : XmenNamespaceHandler.registryDefineNames) {
            RegistryConfig registoryConfig = beanFactory.getBean(name, RegistryConfig.class);
            setRegistry(registoryConfig);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public T getObject() throws Exception {
        return getRef();
    }

    @Override
    public Class<?> getObjectType() {

        return getInterface();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
