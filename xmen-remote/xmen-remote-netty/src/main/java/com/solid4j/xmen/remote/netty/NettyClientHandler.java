/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote.netty;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author solidwang
 * @since 1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private RpcRequest request;

    private RpcResponse response;

    public NettyClientHandler(RpcRequest request) {
        this.request = request;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof RpcResponse) {
            this.response = (RpcResponse) obj;
        }
        ctx.close();
    }

    public RpcResponse getResponse() {

        return response;
    }

    public void setResponse(RpcResponse response) {

        this.response = response;
    }
}
