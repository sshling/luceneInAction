package cn.test.base01;

import com.google.common.io.CharStreams;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 从文件或网络得到一个 InputStream，需要转换成字符串输出或赋给别的变量
 * 1  Apache commons IOUtils，
 * 2  JDK 1.5 后的 Scanner，
 * 3  Google  Guava 库的 CharStreams。
 * 4  JDK7，从文件中直接得到字符串
 *      java.nio.file.Files#readAllLines
 *      java.nio.file.Files#readAllBytes
 *
 *  异常处理+字符集
 *  默认字符集是 System.getProperty("file.encoding")
 *  通常我们都用 UTF-8，异常 UnsupportedEncodingException 继承自 IOException。
 *
 *  其它：用 Groovy，Scala，
 */
public class InputStreamToString {

    @Test
    void t1_JDK5()  {//
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("d:/sample.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
    }
    @Test
    void t1_JDK4_1() throws  Exception {
        //JDK1.4 及之前的 BufferedReader 法
        InputStream inputStream = new FileInputStream("d:/sample.txt");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean firstLine = true;
        String line = null;
        while((line = bufferedReader.readLine()) != null){
            if(!firstLine){
                stringBuilder.append(System.getProperty("line.separator"));
            }else{
                firstLine = false;
            }
            stringBuilder.append(line);
        }
    }
    @Test
    void t1_JDK4_2() throws  Exception {
        //JDK1.4 及之前的 readBytes 法
        //缓冲区的大小自己根据实际来调，比 BufferedReader 还简洁些，不需管换行符的事情。
        InputStream inputStream = new FileInputStream("d:/sample.txt");

        byte[] buffer = new byte[2048];
        int readBytes = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while((readBytes = inputStream.read(buffer)) > 0){
            stringBuilder.append(new String(buffer, 0, readBytes));
        }
    }
    @Test
    void t1_commons() throws  Exception {
        // Apache commons IOUtils.toString 法
        InputStream inputStream = new FileInputStream("d:/sample.txt");
        String text = IOUtils.toString(inputStream,StandardCharsets.UTF_8);
    }
    @Test
    void t2_commons() throws  Exception {
        StringBuilder sb=new StringBuilder();
        sb.append("{ \"delete\": { \"_index\": \"website\", \"_type\": \"blog\", \"_id\": \"123\" }}");
        String url="http://192.168.200.191:40000/_bulk";
        

    }

    @Test
    void t1_guava() throws  Exception {
        // Google guava 的  CharStreams 方法
        InputStream inputStream = new FileInputStream("d:/sample.txt");
        String text = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
    }
    @Test
    void t2_JDK7() throws  Exception {
        //JDK 7 的 NIO readAllBytes
        byte[] bytes = Files.readAllBytes(Paths.get("d:/sample.txt"));
        String text = new String(bytes);
    }

}
