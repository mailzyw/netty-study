package com.zhangyiwen.study.netty.demo5;

import com.zhangyiwen.study.netty.demo5.handler.HeartBeatReqHandler;
import com.zhangyiwen.study.netty.demo5.handler.LoginAuthReqHandler;
import com.zhangyiwen.study.netty.demo5.handler.NettyMessageDecoder;
import com.zhangyiwen.study.netty.demo5.handler.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/11/23.
 */
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        System.out.println(Thread.currentThread().getId()+"connect start");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));          //消息解码器
                            ch.pipeline().addLast("MessageEncoder",new NettyMessageEncoder());      //消息编码器
                            ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50)); //读超时Handler
                            ch.pipeline().addLast("LoginAuthHandler",new LoginAuthReqHandler());    //握手请求Handler
                            ch.pipeline().addLast("HeartBeatHandler",new HeartBeatReqHandler());    //心跳请求Handler
                        }
                    });

            //发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(host,port)).sync();
            System.out.println(Thread.currentThread().getId() + "netty client connect ok.");
            future.channel().closeFuture().sync();
            System.out.println(Thread.currentThread().getId()+"netty client sync over.");
        } finally {
            System.out.println(Thread.currentThread().getId()+"shutdown start.");
            group.shutdownGracefully();
            System.out.println(Thread.currentThread().getId() + "shutdown end.");
            //所有资源释放完成之后,清空资源,再次发起重连操作
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        connect(8080,"127.0.0.1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getId()+"start.");
        new NettyClient().connect(8080,"127.0.0.1");
        System.out.println(Thread.currentThread().getId()+"end.");
    }

}
