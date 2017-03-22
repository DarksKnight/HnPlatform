package cn.ihuoniao.store;

import com.alibaba.fastjson.JSONObject;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AppInfoAction;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/22.
 */

public class AppInfoStore extends Store<AppInfoAction> {

    @Override
    public void onAction(AppInfoAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_GET_APP_INFO:
                getAppInfo();
                break;
            default:
                break;
        }
    }

    private void getAppInfo() {
        webView.registerHandler(Event.GET_APP_INFO, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = new JSONObject();
                json.put("device", "android");
                json.put("version", CommonUtil.getVersionName(activity));
                function.onCallBack(json.toJSONString());
            }
        });
    }
}
