package com.xtj.nio.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 涓
 * @date 2021/2/11
 */
public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6666;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public Client() throws IOException {
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 将channel注册进selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "  is ok。。。");
    }

    public void sendInfo(String info) {
        info = username + "  说:  " + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try{
            int readChannels = selector.select();
            if (readChannels > 0 ) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        // 得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 创建一个buffer
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        sc.read(buf);
                        // 把缓冲区的数据转成字符串
                        String msg = new String(buf.array());
                        System.out.println(msg.trim());

                    }
                }
            } else {
                System.out.println("没有可以用的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        // 启动一个线程用于读取服务器的消息
        new Thread(()->{
            while (true) {
                client.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 主线程用于发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.sendInfo(s);
        }
    }
}
