package com.solid4j.xmen.config.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author: solidwang
 * @date: 17/6/6
 */
public class ServiceConfigTest {

    @Test
    public void ServiceTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("service-test.xml");
        ctx.start();
    }

    @Test
    public void RegistryTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("service-test.xml");
        ctx.start();
        try {
            Map<String, RegistryConfig> map = ctx.getBeansOfType(RegistryConfig.class);
            RegistryConfig object = map.get("registry");
            assertNotNull(map);
            assertNotNull(object);
            assertTrue("127.0.0.1:2181".equals(object.getAddress()));
        } finally {
            ctx.stop();
            ctx.close();
        }
    }

    @Test
    public void ProtocolTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("service-test.xml");
        ctx.start();
        try {
            Map<String, ProtocolConfig> map = ctx.getBeansOfType(ProtocolConfig.class);
            ProtocolConfig object = map.get("xmen");
            ProtocolConfig object2 = map.get("http");
            assertNotNull(object);
            assertNotNull(object2);
        } finally {
            ctx.stop();
            ctx.close();
        }
    }

    @Test
    public void RefererTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("service-test.xml");
        try {
            Map<String, RefererConfigBean> map = ctx.getBeansOfType(RefererConfigBean.class);
            RefererConfigBean object = map.get("&testService");
            assertNotNull(map);
            assertNotNull(object);
        } finally {
            ctx.stop();
            ctx.close();
        }
    }
}