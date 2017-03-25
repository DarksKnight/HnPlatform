package cn.ihuoniao.request;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.API;
import cn.ihuoniao.request.base.BaseRequest;
import cn.ihuoniao.request.base.Request;
import cn.ihuoniao.request.base.RequestCallBack;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppConfigRequest extends BaseRequest<Map<String, Object>> {

    @Override
    public void request(Map<String, Object> params, final RequestCallBack callback) {
        Request.get(API.APP_CONFIG, new HashMap<String, String>(), callback);
    }
}
