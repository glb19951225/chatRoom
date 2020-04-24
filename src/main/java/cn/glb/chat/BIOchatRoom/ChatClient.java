package cn.glb.chat.BIOchatRoom;



import java.io.*;
import java.net.Socket;

/**
 * @author GeLinBo
 * @date 2020/3/8 11:47
 */
public class ChatClient {

    private int DEFAULT_PORT = 9999;
    private String DEFAULT_HOST = "127.0.0.1";
    private Socket socket = null;
    private String QUIT = "quit";

    private BufferedReader reader;
    private BufferedWriter writer;


    public ChatClient() {

    }

    public void send(String msg) throws IOException {
        if(!socket.isOutputShutdown()){

            writer.write(msg + "\n");
            writer.flush();
        }
    }


    public String receive() throws IOException{

        String msg = null;
        if(!socket.isInputShutdown()){
            msg = reader.readLine();

        }

        return msg;
    }


    public boolean readyQuit(String msg) {
        return msg.equals(QUIT);
    }

    public void start(){
        try {
            this.socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //发送消息
            Thread thread = new Thread(new UserInputHandler(this));
            thread.start();

            //接受服务器发来的消息
            String msg = null;
            while((msg = receive()) != null){
                System.out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close(){
        if(writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }

}
