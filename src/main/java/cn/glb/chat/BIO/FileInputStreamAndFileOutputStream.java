package cn.glb.chat.BIO;


import java.io.*;

/**
 * @author GeLinBo
 * @date 2020/4/24 11:15
 */
public class FileInputStreamAndFileOutputStream {

    public static void main(String[] args) {
        File file1 = new File("file1.txt");
        File file3 = new File("file3.txt");
        FileInputStream fin = null;
        FileOutputStream fout = null;

        try {
            fin = new FileInputStream(file1);
            fout = new FileOutputStream(file3);
            byte[] bytes = new byte[10];
            int len = -1;
            while((len = fin.read(bytes)) != -1){
                fout.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                fin.close();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
