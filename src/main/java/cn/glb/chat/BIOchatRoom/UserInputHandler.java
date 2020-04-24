package cn.glb.chat.BIOchatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author GeLinBo
 * @date 2020/3/8 14:46
 */
public class UserInputHandler implements Runnable {

    private ChatClient chatClient;


    public UserInputHandler(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String msg = reader.readLine();
                chatClient.send(msg);

                if(chatClient.readyQuit(msg)){
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
