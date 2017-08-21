/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.demo.consumer;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: solidwang
 * @since 1.0
 */
public class Main {

    @Test
    public void consumer() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("demo-referer.xml");
    }

}
