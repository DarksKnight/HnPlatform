package cn.ihuoniao.function.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class CommonUtil {

    private static boolean isExit = false;

    /**
     * 按两次返回键退出程序
     * @param activity
     */
    public static void exit(Activity activity) {
        if (!isExit) {
            isExit = true;
            Toast.makeText(activity, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isExit = false;
                }
            }.sendEmptyMessageDelayed(0, 2000);
        } else {
            activity.finish();
            System.exit(0);
        }
    }

    /**
     * 是否第一次启动
     * @param context
     * @param fileName
     * @return
     */
    public static boolean isFirstRun(Context context, String fileName) {
        SharedPreferences setting = context.getSharedPreferences(fileName, 0);
        boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {
            setting.edit().putBoolean("FIRST", false).commit();
        }
        return user_first;
    }

    /**
     * 普通提示
     * @param activity
     * @param msg
     */
    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
