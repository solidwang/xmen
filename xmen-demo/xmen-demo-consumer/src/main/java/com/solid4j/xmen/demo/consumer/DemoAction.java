/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.demo.consumer;

import com.solid4j.xmen.demo.service.DemoService;
import com.solid4j.xmen.demo.service.DemoService2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author solidwang
 */
public class DemoAction {

    private DemoService demoService;

    private DemoService2 demoService2;

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

    public void setDemoService2(DemoService2 demoService2) {
        this.demoService2 = demoService2;
    }

    public void start() throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                String hello = demoService.sayHello("world-" + i);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
                String end = demoService2.sayEnd("world-" + i);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + end);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1);
        }
    }
}
