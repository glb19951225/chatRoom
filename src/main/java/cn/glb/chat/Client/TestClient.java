package cn.glb.chat.Client;

import java.io.*;
import java.net.Socket;

/**
 * @author GeLinBo
 * @date 2020/3/7 20:07
 */
public class TestClient {

    private String QUIT = "quit";

    public void createSocket(){


        String DEFAULT_SERVER_HOST = "127.0.0.1";
        int DEFAULT_PORT = 9999;
        Socket socket = null;
        BufferedWriter bfWriter = null;

        try {
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_PORT);
            bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true){

                String message = bufferedReader.readLine();
                if(message.equals(QUIT))
                    break;
                bfWriter.write(message + "\n");
                bfWriter.flush();

                String msg = bfReader.readLine();
                System.out.println(msg);

            }

//            for(int i = 0; i < 10; i++){
//                bfWriter.write(i + "\n");
//                bfWriter.flush();
//                System.out.println("发送了" + i);
//                String msg = bfReader.readLine();
//                System.out.println(msg);
//            }




        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bfWriter != null){
                try {
                    bfWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void main(String[] args) {
        TestClient testClient = new TestClient();
        testClient.createSocket();
    }


}
