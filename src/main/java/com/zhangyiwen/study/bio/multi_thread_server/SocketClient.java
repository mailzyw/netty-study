package com.zhangyiwen.study.bio.multi_thread_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zhangyiwen on 17/2/12.
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket socket =new Socket("127.0.0.1",2017);
            socket.setSoTimeout(60000);

            PrintWriter printWriter =new PrintWriter(socket.getOutputStream(),true);
            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String result ="";
            while(result.indexOf("bye") == -1){
                BufferedReader sysBuff =new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Please input a line of String send to Server...");
                printWriter.println(sysBuff.readLine());
                printWriter.flush();

                result = bufferedReader.readLine();
                System.out.println("Server say : " + result);
            }

            printWriter.close();
            bufferedReader.close();
            socket.close();
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }
}
