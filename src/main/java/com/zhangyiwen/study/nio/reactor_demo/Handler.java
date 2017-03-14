package com.zhangyiwen.study.nio.reactor_demo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by zhangyiwen on 16/11/8.
 */
public interface Handler {

    void handle(SocketChannel socketChannel,SelectionKey sk) throws IOException;
}
