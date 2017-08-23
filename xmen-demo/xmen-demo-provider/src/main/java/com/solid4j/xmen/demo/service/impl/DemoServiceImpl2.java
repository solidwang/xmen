/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.demo.service.impl;

import com.solid4j.xmen.demo.service.DemoService2;

/**
 * @author: solidwang
 * @since 1.0
 */
public class DemoServiceImpl2 implements DemoService2 {

    @Override
    public String sayEnd(String username) {
        return "goodBye:" + username;
    }
}