/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.demo.consumer;

import com.solid4j.xmen.demo.service.DemoService;
import com.solid4j.xmen.demo.service.TestService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author solidwang
 */
public class DemoAction {

    private DemoService demoService;

    private TestService testService;

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    public void start() throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                String hello = demoService.sayHello("world" + i);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
                System.out.println(testService.test());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1);
        }
    }
}
