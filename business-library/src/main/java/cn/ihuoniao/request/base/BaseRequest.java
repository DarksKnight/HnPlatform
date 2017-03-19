package cn.ihuoniao.request.base;

import java.util.Map;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public abstract class BaseRequest {

    public abstract void request(Map<String, Object> params, RequestCallBack callback);
}
