package com.zhangyiwen.study.netty.demo5;

import com.zhangyiwen.study.netty.demo5.handler.HeartBeatRespHandler;
import com.zhangyiwen.study.netty.demo5.handler.LoginAuthRespHandler;
import com.zhangyiwen.study.netty.demo5.handler.NettyMessageDecoder;
import com.zhangyiwen.study.netty.demo5.handler.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by zhangyiwen on 16/11/23.
 */
public class NettyServer {

    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            final HeartBeatRespHandler heartBeatRespHandler = new HeartBeatRespHandler();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));  //消息解码器
                            ch.pipeline().addLast(new NettyMessageEncoder());               //消息编码器
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50)); //读超时Handler
                            ch.pipeline().addLast(new LoginAuthRespHandler());                      //握手响应Handler
                            ch.pipeline().addLast(new HeartBeatRespHandler());                      //心跳响应Handler
                        }
                    });

            //配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture future = b.bind("127.0.0.1", 8080).sync();
            System.out.println(Thread.currentThread().getId()+"Netty server start ok");
            // 应用程序会一直等待，直到channel关闭
            future.channel().closeFuture().sync();
            System.out.println(Thread.currentThread().getId()+"netty server sync over.");
        } finally {
            System.out.println(Thread.currentThread().getId()+"shutdown start.");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println(Thread.currentThread().getId()+"shutdown end.");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getId()+"start.");
        new NettyServer().bind();
        System.out.println(Thread.currentThread().getId()+"end.");
    }

}
