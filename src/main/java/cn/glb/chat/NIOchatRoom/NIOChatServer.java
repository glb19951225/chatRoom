package cn.glb.chat.NIOchatRoom;

import javax.print.attribute.standard.Severity;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author GeLinBo
 * @date 2020/3/9 11:27
 */
public class NIOChatServer {

    private int DEFAULT_PORT = 9999;
    private String QUIT = "quit";

    private static final int BUFFER = 1024;

    private ServerSocketChannel server;
    private int port;
    private Selector selector;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");


    public NIOChatServer(){
        this.port = DEFAULT_PORT;
    }

    public NIOChatServer(int port){
        this.port = port;

    }


    public void start(){

        try {
            server = ServerSocketChannel.open();
            //改为非阻塞
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器运行，正在监听.." + port + "端口");

            while(true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey key : selectionKeys){
                    handles(key);
                }
                selectionKeys.clear();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public synchronized void forwardMessage(SocketChannel socket, String message) throws IOException {
        int port = socket.socket().getPort();

        Set<SelectionKey> set = selector.keys();
        for(SelectionKey key : set){

            Channel channel = key.channel();

            if(channel instanceof ServerSocketChannel){
                continue;
            }

            if(key.isValid() && !socket.equals(channel)){
                wBuffer.clear();
                message = "客户端【" + socket.socket().getPort() + "】说：" + message;
                wBuffer.put(charset.encode(message));
                wBuffer.flip();
                while(wBuffer.hasRemaining()){
                    ((SocketChannel) channel).write(wBuffer);
                }

            }

        }
    }

    public synchronized void handles(SelectionKey key) throws IOException {

        //accept事件  与客户端建立连接
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端：" + client.socket().getPort() + "已经连接");

        } else if(key.isReadable()){  //客户端发送了消息，转发消息
            SocketChannel client = (SocketChannel) key.channel();

            String msg = receive(client);
            System.out.println(msg + "msg1");

            if(msg.isEmpty()){
                //客户端异常
                System.out.println("rr");
                key.cancel();
                selector.wakeup();
            } else {

                forwardMessage(client, msg);
                if(msg.equals(QUIT)){
                    key.cancel();
                    selector.wakeup();
                    System.out.println("客户端：" + client.socket().getPort()+ "断开连接");
                }
            }
        }

    }


    public String receive(SocketChannel client) throws IOException {
        System.out.println("receive");
        rBuffer.clear();
        while(client.read(rBuffer) > 0);

        rBuffer.flip();
        System.out.println(String.valueOf(charset.decode(rBuffer)) + " pppp");
        return String.valueOf(charset.decode(rBuffer));
    }


    public static void main(String[] args) {
        NIOChatServer nioChatServer = new NIOChatServer();
        nioChatServer.start();
    }
}
