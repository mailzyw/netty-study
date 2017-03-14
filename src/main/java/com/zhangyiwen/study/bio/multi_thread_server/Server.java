package com.zhangyiwen.study.bio.multi_thread_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zhangyiwen on 17/2/12.
 */
public class Server extends ServerSocket{
    private static final int SERVER_PORT =2017;

    public Server()throws IOException {
        super(SERVER_PORT);
        this.setReceiveBufferSize(1024);
        try {
            while (true) {
                Socket socket = this.accept();
                new CreateServerThread(socket).start();//当有请求时，启一个线程处理
            }
        }catch (IOException e) {
        }finally {
            this.close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;

        public CreateServerThread(Socket s)throws IOException {
            client = s;
            bufferedReader =new BufferedReader(new InputStreamReader(client.getInputStream()));
            printWriter =new PrintWriter(client.getOutputStream(),true);

            System.out.println("Client(" + getName() +") come in...");
        }

        public void run() {
            try {
                String line = bufferedReader.readLine();
                System.out.println("line ---> " + line);

                while (line != null && !line.equals("bye")) {
                    System.out.println("Client(" + getName() +") say: " + line);
                    printWriter.println(line.toUpperCase());
                    printWriter.flush();
                    line = bufferedReader.readLine();
                }
                printWriter.println("bye, Client(" + getName() +")!");
                printWriter.flush();

                System.out.println("Client(" + getName() +") exit!");
                printWriter.close();
                bufferedReader.close();
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)throws IOException {
        new Server();
    }
}
