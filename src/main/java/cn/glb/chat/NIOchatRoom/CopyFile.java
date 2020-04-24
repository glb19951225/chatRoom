package cn.glb.chat.NIOchatRoom;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GeLinBo
 * @date 2020/3/8 22:16
 */

interface CopyFileInter{

    void copyFile(File spurce, File target);
}

public class CopyFile {


    static CopyFileInter copyFileInter = null;

    public static void main(String[] args) {




        copyFileInter = new CopyFileInter() {
            @Override
            public void copyFile(File source, File target) {
                FileChannel fin = null;
                FileChannel fout = null;

                try {
                    fin = new FileInputStream(source).getChannel();
                    fout = new FileOutputStream(target).getChannel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    while((fin.read(buffer) != -1)){
                        //转为读模式
                        buffer.flip();

                        //若buffer中还有数据那么继续写
                        while(buffer.hasRemaining()){

                            //写入通道channel
                            fout.write(buffer);
                        }

                        //转为写入模式
                        buffer.clear();

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
        };



        copyFileInter = new CopyFileInter() {
            @Override
            public void copyFile(File spurce, File target) {

            }
        };

    }



}
