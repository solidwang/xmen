/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;
/**
 * @author solidwang
 */
public class TestServiceImpl implements TestService {

    @Override
    public String hello(String name) {

        return "hello-" + name;
    }

}

    