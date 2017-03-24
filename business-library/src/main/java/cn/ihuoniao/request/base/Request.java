package cn.ihuoniao.request.base;


import com.mistyrain.http.HttpUtility;
import com.mistyrain.http.callback.StringCallback;
import com.mistyrain.http.config.HttpMethod;

import java.util.Map;

import cn.ihuoniao.function.util.Logger;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public class Request {

    public static void get(String api, Map<String, String> params, final RequestCallBack callback) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, api, params, new StringCallback() {
                @Override
                public void onResponse(String s) {
                    Logger.i("request : " + s);
                    callback.onSuccess(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(String api, Map<String, String> params, final RequestCallBack callback) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, api, params, new StringCallback() {
                @Override
                public void onResponse(String s) {
                    Logger.i("request : " + s);
                    callback.onSuccess(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
