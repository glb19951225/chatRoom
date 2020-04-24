package cn.glb.chat.BIOchatRoom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author GeLinBo
 * @date 2020/3/8 11:47
 */
public class ChatServer {


    private int DEFAULT_PORT = 9999;
    private String QUIT = "quit";
    private ServerSocket serverSocket = null;
    private Map<Integer, Writer> mapOfClients;
    private ExecutorService executorService;

    public ChatServer(){

        executorService = Executors.newFixedThreadPool(10);
        mapOfClients = new HashMap<>();
    }

    public synchronized void addClient(Socket socket) throws IOException {
        if(socket != null){
            int port = socket.getPort();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            mapOfClients.put(port, writer);
            System.out.println("客户端：" + port + " 已连接到服务器");
        }

    }

    public synchronized void removeClient(Socket socket) throws IOException {
        if(mapOfClients != null){
            int port = socket.getPort();
            if(mapOfClients.containsKey(port)){
                mapOfClients.get(port).close();
                mapOfClients.remove(port);
            }
            System.out.println("客户端：" + port + " 已经下线了");
        }
    }

    public synchronized void forwardMessage(Socket socket, String message) throws IOException {
        int port = socket.getPort();

        Set<Integer> set = mapOfClients.keySet();
        for(Integer curPort : set){

            if(!curPort.equals(port)){

                Writer writer = mapOfClients.get(curPort);
                writer.write(port + " 发送了消息：" + message + "\n");
                writer.flush();
            }

        }
    }

    public synchronized void close(){

        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void start(){
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器已经启动，正在监听" + DEFAULT_PORT + "端口，等待连接...");
            while(true){
                Socket socket = serverSocket.accept();
                //Handler线程
                ChatHandler chatHandler = new ChatHandler(this, socket);
//                Thread thread = new Thread(chatHandler);
//                thread.start();
                executorService.execute(chatHandler);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
