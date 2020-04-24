package cn.glb.chat.BIO;

import java.io.*;

/**
 *
 * 处理流之一：缓冲流的使用
 *
 * 1.缓冲流：
 * BufferedInputStream
 * BufferedOutputStream
 * BufferedReader
 * BufferedWriter
 *
 * 2.作用：提供流的读取、写入的速度
 *  提高读写速度的原因：内部提供了一个缓冲区
 *
 * 3. 处理流，就是“套接”在已有的流的基础上。
 * @author GeLinBo
 * @date 2020/4/24 13:42
 */
public class BufferedTest {

    static File file1 = new File("file1.txt");
    static File file4 = new File("file4.txt");
    static File file5 = new File("file5.txt");

    public static void main(String[] args) {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        BufferedInputStream bfin = null;
        BufferedOutputStream bfout = null;

        try {
            fin = new FileInputStream(file1);
            fout = new FileOutputStream(file4);
            bfin = new BufferedInputStream(fin);
            bfout = new BufferedOutputStream(fout);

            byte[] bytes = new byte[10];
            int len = -1;
            while((len = bfin.read(bytes)) != -1){
                bfout.write(bytes, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //先关闭外部的流，再关闭内部的流
            try {
                bfin.close();
                fin.close();
                bfout.close();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader bfr = null;
        BufferedWriter bfw = null;

        try {
            fr = new FileReader(file1);
            fw = new FileWriter(file5);
            bfr = new BufferedReader(fr);
            bfw = new BufferedWriter(fw);

            char[] buffers = new char[10];
            int len = -1;
            while((len = bfr.read(buffers)) != -1){
                bfw.write(buffers, 0, len);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                bfr.close();
                bfw.close();
                fr.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }



}
