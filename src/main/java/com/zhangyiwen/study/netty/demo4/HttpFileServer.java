package com.zhangyiwen.study.netty.demo4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by zhangyiwen on 16/11/18.
 */
public class HttpFileServer {

    private static final String DEFAULT_URL = "/Users/zhangyiwen/";

    public void run(final int port,final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //HTTP请求消息解码器
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //将多个消息转换为单一的FullHttpRequest或者FullHttpResponse(HTTP解码器在每个HTTP消息中会生成多个消息对象HttpReqeust,HttpResponse,HttpContent,LastHttpContent)
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            //HTTP响应消息编码器
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            //支持异步发送大的码流
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler());
                        }
                    });
            ChannelFuture future = b.bind("127.0.0.1",port).sync();
            System.out.println("HTTP文件目录服务器启动,网址是 http://127.0.0.1:"+port+url);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        String url = DEFAULT_URL;
        if(args.length > 1){
            url = args[1];
        }
        new HttpFileServer().run(port,url);
    }

}
