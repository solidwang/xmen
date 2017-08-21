/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote.server;


import com.solid4j.xmen.remote.Server;

/**
 * @author solidwang
 * @since 1.0
 */
public abstract class AbstractServer implements Server {

    public abstract void doOpen();

    public abstract void doClose();

}
