package cn.ihuoniao.request;

import com.platform.http.HttpUtility;
import com.platform.http.callback.StringCallback;
import com.platform.http.config.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.API;
import cn.ihuoniao.request.base.BaseRequest;
import cn.ihuoniao.request.base.RequestCallBack;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppConfigRequest extends BaseRequest {

    @Override
    public void request(Map<String, Object> params, final RequestCallBack callback) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, API.APP_CONFIG, new HashMap<String, String>(), new StringCallback() {
                @Override
                public void onResponse(String s) {
                    callback.onSuccess(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
