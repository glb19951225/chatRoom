package cn.glb.chat.BIO;

import java.io.*;

/**
 *
 *
 * 处理流之二：转换流的使用
 * 1.转换流：属于字符流
 *   InputStreamReader：将一个字节的输入流转换为字符的输入流
 *   OutputStreamWriter：将一个字符的输出流转换为字节的输出流
 *
 * 2.作用：提供字节流与字符流之间的转换
 *
 * 3. 解码：字节、字节数组  --->字符数组、字符串
 *    编码：字符数组、字符串 ---> 字节、字节数组
 *
 *
 * 4.字符集
 *ASCII：美国标准信息交换码。
 用一个字节的7位可以表示。
 ISO8859-1：拉丁码表。欧洲码表
 用一个字节的8位表示。
 GB2312：中国的中文编码表。最多两个字节编码所有字符
 GBK：中国的中文编码表升级，融合了更多的中文文字符号。最多两个字节编码
 Unicode：国际标准码，融合了目前人类使用的所有字符。为每个字符分配唯一的字符码。所有的文字都用两个字节来表示。
 UTF-8：变长的编码方式，可用1-4个字节来表示一个字符。
 * @author GeLinBo
 * @date 2020/4/24 14:19
 */
public class InputStreamReaderAndOutputStreamWriter {



    public static void main(String[] args) {
        //InputStreamReader和OutputStreamWriter都属于字符流，读入是将字节流转为内存中的字符楼，写出是将字符流转换为字节流

        FileInputStream fin = null;
        FileOutputStream fout = null;
        InputStreamReader ir = null;
        OutputStreamWriter ow = null;

        try {
            fin = new FileInputStream("file1.txt");
            ir = new InputStreamReader(fin);

            char[] cbf = new char[100];
            int len = -1;
            while((len = ir.read(cbf)) != -1){
                System.out.println(new String(cbf, 0, len));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
                ir.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            fin = new FileInputStream("file1.txt");
            ir = new InputStreamReader(fin);
            fout = new FileOutputStream("file6.txt");
            ow = new OutputStreamWriter(fout);

            char[] cbf = new char[20];
            int len = -1;
            while((len = ir.read(cbf)) != -1){
                ow.write(cbf, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ir.close();
                ow.close();
                fin.close();
                fout.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }


    }
}
