package cn.glb.chat.BIO;

import java.io.*;


/**
 * @author GeLinBo
 * @date 2020/4/24 10:54
 */
public class FileReaderAndFileWriter {

    public static void main(String[] args) {
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");

        FileReader fr = null;
        FileWriter fw = null;
        try {
            fr = new FileReader(file1);
            fw = new FileWriter(file2);
            char[] buffers = new char[10];
            int len = -1;
            while((len = fr.read(buffers)) != -1){
                fw.write(buffers, 0, len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            try {
                fr.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




}
