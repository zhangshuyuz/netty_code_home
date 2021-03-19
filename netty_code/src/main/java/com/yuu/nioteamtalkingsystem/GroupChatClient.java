package com.yuu.nioteamtalkingsystem;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {
    // 首先，写属性
    private final String HOST;
    private final int PORT;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() {
        this.HOST = "127.0.0.1";
        this.PORT = 8800;
        init();
    }

    private void init() {
        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open(new InetSocketAddress(this.HOST, this.PORT));
            this.socketChannel.configureBlocking(false);
            this.socketChannel.register(this.selector, SelectionKey.OP_READ);

            this.username = this.socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + "已经准备完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    public void sendMsg(String msg) {
        try {
            msg = username + "说：" + msg;
            ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
            this.socketChannel.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取消息
     */
    public void readServerMsg() {
        try {
            int selects = selector.select();

            if (selects > 0) {

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for (SelectionKey selection :
                        selectionKeys) {
                    if (selection.isReadable()) {
                        SocketChannel channel = (SocketChannel) selection.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int read = channel.read(readBuffer);

                        System.out.println(new String(readBuffer.array(), 0, read));
                    }
                    selectionKeys.remove(selection);
                }

            } else {
//                System.out.println("没有可用的通道");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        GroupChatClient groupChatClient = new GroupChatClient();

        // 每三秒，读取服务端发送来的数据
        new Thread(() -> {
            while (true) {
                groupChatClient.readServerMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            groupChatClient.sendMsg(s);
        }
    }
}
