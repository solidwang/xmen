/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring.handler;

import com.solid4j.xmen.config.spring.ProtocolConfig;
import com.solid4j.xmen.config.spring.RegistryConfig;
import com.solid4j.xmen.config.spring.ServiceConfigBean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author solidwang
 * @since 1.0
 */
public class XmenBeanDefinitionParser implements BeanDefinitionParser {

    private final Class<?> beanClass;
    private final boolean required;

    public XmenBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }

    /**
     * 解析xml文件
     * @param element
     * @param parseContext
     * @return
     */
    @Override
    public BeanDefinition parse(Element element, ParserContext parseContext) {
        try {
            return parse(element, parseContext, beanClass, required);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) throws ClassNotFoundException {
        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(beanClass);
        bd.setLazyInit(false);
        String id = element.getAttribute("id");
        if ((id == null || id.length() == 0) && required) {
            String generatedBeanName = element.getAttribute("name");
            if (generatedBeanName == null || generatedBeanName.length() == 0) {
                generatedBeanName = element.getAttribute("class");
            }
            if (generatedBeanName == null || generatedBeanName.length() == 0) {
                generatedBeanName = beanClass.getName();
            }
            id = generatedBeanName;
            int counter = 2;
            while (parserContext.getRegistry().containsBeanDefinition(id)) {
                id = generatedBeanName + (counter++);
            }
        }
        if (id != null) {
            if (parserContext.getRegistry().containsBeanDefinition(id)) {
                throw new IllegalStateException("Duplicate spring bean config, id = " + id);
            }
            parserContext.getRegistry().registerBeanDefinition(id, bd);
        }
        bd.getPropertyValues().addPropertyValue("id", id);

        if (ProtocolConfig.class.equals(beanClass)) {
            XmenNamespaceHandler.protocolDefineNames.add(id);
        } else if (RegistryConfig.class.equals(beanClass)) {
            XmenNamespaceHandler.registryDefineNames.add(id);
        } else if (ServiceConfigBean.class.equals(beanClass)) {
            XmenNamespaceHandler.serviceDefineName.add(id);
            String className = element.getAttribute("class");
            if (className != null && className.length() > 0) {
                RootBeanDefinition classDefinition = new RootBeanDefinition();
                classDefinition.setBeanClass(Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
                classDefinition.setLazyInit(false);
                bd.getPropertyValues().addPropertyValue("ref", new BeanDefinitionHolder(classDefinition, id + "Impl"));
            }
        }
        for (Method setter : beanClass.getMethods()) {
            String name = setter.getName();
            if (name.length() <= 3 || !name.startsWith("set") || !Modifier.isPublic(setter.getModifiers())) {
                continue;
            }
            String property = name.substring(3, 4).toLowerCase() + name.substring(4);
            if ("id".equals(property)) {
                bd.getPropertyValues().addPropertyValue("id", id);
                continue;
            }
            String value = element.getAttribute(property);
            value = value.trim();
            if (value.length() == 0 || value == null) {
                continue;
            }
            Object reference;
            if ("ref".equals(property)) {
                reference = new RuntimeBeanReference(value);
            } else {
                reference = new TypedStringValue(value);
            }
            bd.getPropertyValues().addPropertyValue(property, reference);
        }
        return bd;
    }
}
