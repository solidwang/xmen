/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote.netty;

import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.remote.netty.codec.RpcDecoder;
import com.solid4j.xmen.remote.netty.codec.RpcEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author solidwang
 * @since 1.0
 */
public class NettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcResponse send(RpcRequest request) throws Exception {
        //可以优化线程个数
        EventLoopGroup group = new NioEventLoopGroup(2);
        RpcResponse obj = null;
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            final NettyClientHandler handler = new NettyClientHandler(request);
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(handler); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.closeFuture().sync();
            // 返回 RPC 响应对象
            obj = handler.getResponse();
        } finally {
            group.shutdownGracefully();
        }
        return obj;
    }
}
