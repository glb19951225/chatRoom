package cn.glb.chat.NIOchatRoom;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author GeLinBo
 * @date 2020/3/9 11:27
 */
public class NIOChatClient {

    private static int DEFAULT_PORT = 9999;
    private static String DEFAULT_HOST = "127.0.0.1";
    private Socket socket = null;
    private String QUIT = "quit";
    private static final int BUFFER = 1024;

    private SocketChannel client;
    private int port;
    private String host;
    private Selector selector;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");


    public NIOChatClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public NIOChatClient(String host, int port) {
        this.host = host;
        this.port = port;

    }


    public void start(){
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);

            selector = Selector.open();
            client.register(selector, SelectionKey.OP_CONNECT);
            client.connect(new InetSocketAddress(host, port));
            while(true){

                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                for(SelectionKey key : selectionKeySet){

                    handles(key);
                }
                selectionKeySet.clear();
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


    public void handles(SelectionKey key) throws IOException{
        //connect事件
        if(key.isConnectable()){
            SocketChannel channel = (SocketChannel) key.channel();
            if(channel.isConnectionPending()){
                client.finishConnect();
                //处理用户输入

                new Thread(new UserInputHandler(this)).start();
            }
            client.register(selector, SelectionKey.OP_READ);
        } else if(key.isReadable()){ //处理服务器发来新消息
            SocketChannel client = (SocketChannel) key.channel();
            String msg = read(client);
            if(msg == null){
                selector.close();
            } else {
                System.out.println(msg);
            }
        }


    }

    public String read(SocketChannel client) throws IOException{
        rBuffer.clear();
        while(client.read(rBuffer) > 0);
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    public void send(String msg) throws IOException{
        if(msg.isEmpty())
            return;

        wBuffer.clear();
        wBuffer.put(charset.encode(msg));
        //将Buffer转为读模式
        wBuffer.flip();
        while(wBuffer.hasRemaining()){
            //将Buffer中的数据读入到Channel中
            client.write(wBuffer);
        }


        System.out.println(msg + "send");
        if(msg.equals(QUIT))
            selector.close();
    }


    public static void main(String[] args) {
        NIOChatClient nioChatClient = new NIOChatClient();

        nioChatClient.start();
    }
}
