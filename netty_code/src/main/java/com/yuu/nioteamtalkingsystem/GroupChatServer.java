package com.yuu.nioteamtalkingsystem;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * NIO群聊系统服务端
 */
public class GroupChatServer {

    // 首先，定义需要的属性
    private Selector selector;

    private ServerSocketChannel listenServerSocketChannel;

    private static final InetSocketAddress LISTEN_PORT = new InetSocketAddress(8800);

    public GroupChatServer() {
        // 初始化
        this.init();
    }

    private void init() {

        try {
            // 初始化Selector和ServerSocketChannel
            this.selector = Selector.open();
            this.listenServerSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            this.listenServerSocketChannel.socket().bind(LISTEN_PORT);
            // 非阻塞
            this.listenServerSocketChannel.configureBlocking(false);
            // 注册ServerSocketChannel
            this.listenServerSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 监听
     */
    public void listen() {
        try {

            while (true) {

                // 监控所有的Channel
                int select = this.selector.select();

                if (select > 0) {
                    // 相关Channel有事件要处理

                    // 获取发生事件的Channel的SelectionKey的集合
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();

                    // 处理事件
                    for (SelectionKey selection:
                         selectionKeys) {

                        // 客户端连接
                        if (selection.isAcceptable()) {
                            // 给客户端分配SocketChannel
                            SocketChannel accept = this.listenServerSocketChannel.accept();
                            // 非阻塞
                            accept.configureBlocking(false);
                            // 注册
                            accept.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                            // 打印客户端上线信息
                            System.out.println("客户端[" + accept.getRemoteAddress() + "]：已经上线");
                        }

                        // Channel中有数据要读取
                        if (selection.isReadable()) {
                            // 调用处理读事件的方法readData()
                            readData(selection);
                        }

                        // 处理完毕，移除SelectionKey
                        selectionKeys.remove(selection);

                    }

                } else {
                    System.out.println("等待中");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取消息的方法
     */
    private void readData(SelectionKey selection) {

        SocketChannel socketChannel = null;
        ByteBuffer readBuffer = null;

        try {
            // 反向获取SocketChannel
            socketChannel = (SocketChannel)selection.channel();
            // 获取缓冲
            readBuffer = (ByteBuffer)selection.attachment();

            // 读取Channel数据，放入Buffer
            int read = socketChannel.read(readBuffer);

            // 根据read处理
            if (read > 0) {
                // 将缓冲区数据转为String
                String data = new String(readBuffer.array(), 0, read);
                // 输出消息
                System.out.println("客户端[" + socketChannel.getRemoteAddress() + "]消息是：" + data);
                // 向其他客户端转发消息
                // 调用转发消息的方法dispatcherMessageToOtherClients()
                dispatcherMessageToOtherClients(data, socketChannel);
            }

            // 处理完毕，清空Buffer
            readBuffer.clear();

        } catch (Exception e) {
            try {
                System.out.println("客户端[" + socketChannel.getRemoteAddress() + "]：" + "已经离线");
                // 离线，先取消注册
                selection.cancel();
                // 然后关闭通道
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    /**
     * 转发消息给其他客户端
     */
    private void dispatcherMessageToOtherClients(String data, SocketChannel selfChannel) {
        // 转发给其他客户端，需要排除自己和ServerSocketChannel
        System.out.println("服务器开始转发消息");

        // 遍历所有注册过的Channel
        try {
            Set<SelectionKey> allChannelSelectionKeys = this.selector.keys();
            for (SelectionKey selection :
                    allChannelSelectionKeys) {
                SelectableChannel channel = selection.channel();
                // 通过学过的instanceof 来判断
                // 首先排除ServerSocketChannel
                if (channel instanceof SocketChannel) {
                    // 其次排除自己
                    if (!channel.equals(selfChannel)) {
                        // 进行转发消息

                        SocketChannel otherSocketChannel = (SocketChannel)channel;
                        ByteBuffer msgBuffer = ByteBuffer.wrap(data.getBytes());
                        // 将Buffer数据写入Channel
                        otherSocketChannel.write(msgBuffer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
