package cn.glb.chat.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author GeLinBo
 * @date 2020/3/7 19:54
 */
public class TestServer {



    private String QUIT = "quit";
    private int DEFAULT_PORT = 9999;
    private ServerSocket serverSocket = null;

    public void createServerSocket(){

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器启动，正在监听" + DEFAULT_PORT + "端口");
            while(true){
                Socket socket = serverSocket.accept();
                BufferedReader bfReder = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String msg = null;
                while (true) {
                    msg = bfReder.readLine();
                    if(msg != null){

                        if(msg.equals(QUIT))
                            break;
                        System.out.println(msg);
                        bfWriter.write("服务端发来消息：" + msg + " test \n");
                        bfWriter.flush();
                        System.out.println("发送结束");
                    } else {
                        System.out.println("消息为空！");
                        break;
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭serverSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        testServer.createServerSocket();
    }
}
