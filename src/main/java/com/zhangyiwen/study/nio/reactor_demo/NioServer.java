package com.zhangyiwen.study.nio.reactor_demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Created by zhangyiwen on 16/11/7.
 * 服务端,会先发欢迎语,后续会将客户端发来的消息转成大写后返回
 */
public class NioServer {

    private InetAddress             hostAddress;
    private int                     port;
    private Selector                selector;
    private Selector                subSelector;
    private ServerSocketChannel     serverChannel;
    private Acceptor                acceptor            = new Acceptor();


    public NioServer(InetAddress hostAddress, int port) throws IOException {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public static void main(String[] args) {
        try {
            // 启动服务器
            NioServer nioServer = new NioServer(null, 9090);
            nioServer.initSelector();
//            // 启动subReactor线程
//            nioServer.startSub();
            // 主线程执行Reactor
            nioServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化selector,初始化ServerSocketChannel,将ServerSocketChannel的OP_ACCEPT注册到Selector上
     * @return
     * @throws IOException
     */
    private void initSelector()throws IOException {
        /**
         * 初始化Selector
         */
        selector = SelectorProvider.provider().openSelector();
        subSelector = SelectorProvider.provider().openSelector();

        /**
         * 初始化ServerSocketChannel
         */
        // 创建并打开ServerSocketChannel
        serverChannel = ServerSocketChannel.open();
        // 设置为非阻塞
        serverChannel.configureBlocking(false);
        // 绑定端口
        serverChannel.socket().bind(new InetSocketAddress(hostAddress, port));

        /**
         * 将ServerSocketChannel的OP_ACCEPT注册到Selector上
         */
        // 用selector注册套接字，并返回对应的SelectionKey，同时设置Key的interest set为监听客户端连接事件
        SelectionKey selectionKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 主Reactor线程监听并处理就绪的网络事件(accept,read,write)
     */
    public void start() {
        System.out.println("selector thread start run...");
        while (true) {
            /*
             * 选择事件已经ready的selectionKey,该方法是阻塞的.
             * 只有当至少存在selectionKey,或者wakeup方法被调用,或者当前线程被中断,才会返回.
             */
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 循环处理每一个事件
            Iterator<SelectionKey> items = selector.selectedKeys().iterator();
            while (items.hasNext()) {
                SelectionKey key = items.next();
                items.remove();
                if (!key.isValid()) {
                    continue;
                }
                // 事件处理分发
                dispatch(key);
            }
        }
    }

//    /**
//     * subReactor线程监听并处理就绪的网络事件(read,write)
//     */
//    public void startSub() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("subSelector thread start run...");
//                while (true) {
//                    try {
//                        subSelector.select();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    // 循环处理每一个事件
//                    Iterator<SelectionKey> items = subSelector.selectedKeys().iterator();
//                    while (items.hasNext()) {
//                        SelectionKey key = items.next();
//                        items.remove();
//                        if (!key.isValid()) {
//                            continue;
//                        }
//                        // 事件处理分发
//                        dispatch(key);
//                    }
//                }
//            }
//        }).start();
//    }

    /**
     * 事件处理分发
     * @param sk 已经ready的selectionKey
     */
    private void dispatch(SelectionKey sk){
        try {
            if(sk.isAcceptable()){
                acceptor.accept((ServerSocketChannel)sk.channel(),selector);
            }
            else {
                Handler handler = (Handler) sk.attachment();
                if (handler != null) {
                    handler.handle((SocketChannel)sk.channel(),sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if(sk.channel()!=null){
                    sk.channel().close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
