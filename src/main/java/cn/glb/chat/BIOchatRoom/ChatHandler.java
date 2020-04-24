package cn.glb.chat.BIOchatRoom;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * @author GeLinBo
 * @date 2020/3/8 12:51
 */
public class ChatHandler implements Runnable {


    private ChatServer chatServer;
    private Socket socket;
    private String QUIT = "quit";

    @Override
    public void run() {
        try {
            chatServer.addClient(socket);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = null;

            while (true) {
                msg = reader.readLine();
                if(msg != null){
                    String fwdMessage = "客户端：" + socket.getPort() + "发来消息 " + msg;
                    System.out.println(fwdMessage);
                    chatServer.forwardMessage(socket, msg);
                    if(msg.equals(QUIT)){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                chatServer.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ChatHandler(ChatServer chatServer, Socket socket){
        this.chatServer = chatServer;
        this.socket = socket;
    }

}
