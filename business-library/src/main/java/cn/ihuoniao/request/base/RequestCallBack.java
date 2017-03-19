package cn.ihuoniao.request.base;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public interface RequestCallBack {

    void onSuccess(String content);

    void onFail(String code, String msg);
}
