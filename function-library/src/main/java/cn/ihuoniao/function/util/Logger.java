package cn.ihuoniao.function.util;

import android.util.Log;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class Logger {

    private final static String TAG = Logger.class.getSimpleName();

    public static void i(String str) {
        Log.i(TAG, str);
    }
}
