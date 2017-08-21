/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

/**
 * @author solidwang
 */
public class Test2ServiceImpl implements Test2Service {

    @Override
    public String hello(String name) {
        return "hello2-" + name;
    }

}
