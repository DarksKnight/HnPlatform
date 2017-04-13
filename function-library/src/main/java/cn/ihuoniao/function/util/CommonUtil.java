package cn.ihuoniao.function.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
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
     *
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

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * 调用系统相册
     *
     * @param activity
     * @param code
     */
    public static void openFunction(Activity activity, int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "选择"), code);
    }

    /**
     * 获取缓存总数
     * @param context
     * @return
     */
    public static String getTotalCacheSize(Context context) {
        try {
            long cacheSize = getFolderSize(context.getCacheDir());
            return getFormatSize(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 清理缓存
     * @param context
     */
    public static void clearCache(Context context) {
        try {
            for (File item : context.getCacheDir().listFiles()) {
                item.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        BigDecimal result2 = new BigDecimal(Double.toString(size / 1024 / 1024));
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString();
    }
}
