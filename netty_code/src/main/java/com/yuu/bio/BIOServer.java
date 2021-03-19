package com.yuu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    /*
        思路分析
        1、创建线程池
        2、如果有客户端连接， 创建一个线程，执行通讯方法
     */
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(8000);

        System.out.println("服务器已经启动");

        while (true) {
            // 监听等待客户端连接
            final Socket accept = serverSocket.accept();
            // 提示
            System.out.println("客户端已经连接");

            // 创建线程与之通信
            executorService.execute(() -> {
                handler(accept);
            });

        }
    }

    // 通信的方法
    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            // 通过Socket获取输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                // 输出客户端发送的数据
                String s = new String(bytes, 0, len);
                System.out.println("客户端的数据为：" + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
