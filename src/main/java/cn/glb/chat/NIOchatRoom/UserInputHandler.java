package cn.glb.chat.NIOchatRoom;

import cn.glb.chat.BIOchatRoom.ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author GeLinBo
 * @date 2020/3/8 14:46
 */
public class UserInputHandler implements Runnable {

    private NIOChatClient chatClient;
    private String QUIT = "quit";

    public UserInputHandler(NIOChatClient chatClient){
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String msg = reader.readLine();
                System.out.println(msg);
                chatClient.send(msg);

                if(msg.equals(QUIT)){
                    break;
                }




            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }
}
