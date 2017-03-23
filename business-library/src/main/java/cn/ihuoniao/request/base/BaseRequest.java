package cn.ihuoniao.request.base;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public abstract class BaseRequest<T> {

    public abstract void request(T params, RequestCallBack callback);
}
