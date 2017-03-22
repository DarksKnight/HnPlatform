package cn.ihuoniao.function.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class CommonUtil {

    private static boolean isExit = false;

    /**
     * 按两次返回键退出程序
     *
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
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean isFirstRun(Context context, String fileName) {
        SharedPreferences setting = context.getSharedPreferences(fileName, 0);
        boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {
            Set<String> advUrls = new HashSet<>();
            setting.edit().putStringSet("advUrls", advUrls);
            setting.edit().putBoolean("FIRST", false).commit();
        }
        return user_first;
    }

    /**
     * 是否显示广告
     *
     * @param context
     * @param fileName
     * @param url
     * @return
     */
    public static boolean isShowAdv(Context context, String fileName, String url) {
        SharedPreferences setting = context.getSharedPreferences(fileName, 0);
        Set<String> advUrls = setting.getStringSet("advUrls", new HashSet<String>());
        if (!advUrls.contains(url)) {
            advUrls.add(url);
            setting.edit().putStringSet("advUrls", advUrls).commit();
            return true;
        }
        return false;
    }

    /**
     * 普通提示
     *
     * @param activity
     * @param msg
     */
    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否测试模式
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void bomb() {
        long time = (new Date()).getTime();
        if (time > 1493946068000l) {
            System.exit(0);
        }
    }
}
