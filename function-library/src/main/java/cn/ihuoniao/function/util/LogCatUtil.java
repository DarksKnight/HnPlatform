package cn.ihuoniao.function.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sdk-app-shy on 2017/3/18.
 */

public class LogCatUtil {

    public static String getLog() {
        try {
            ArrayList<String> cmdLine = new ArrayList<>();   //设置命令   logcat -d 读取日志
            cmdLine.add("logcat");
            cmdLine.add("-d");

            Process process = Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));   //捕获日志
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));    //将捕获内容转换为BufferedReader

            String str = "";
            String log = "";
            while ((str = bufferedReader.readLine()) != null)    //开始读取日志，每次读取一行
            {
                log += str + "\n";
            }
            return log;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
