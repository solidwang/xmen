/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

/**
 * @author solidwang
 * @since 1.0
 */
public abstract class AbstractConfig {

    private String id;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

}
