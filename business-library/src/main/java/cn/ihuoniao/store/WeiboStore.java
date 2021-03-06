package cn.ihuoniao.store;

import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.WeiboAction;
import cn.ihuoniao.function.command.WeiboLoginCommand;
import cn.ihuoniao.function.receiver.WeiboLoginReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboStore extends Store<WeiboAction> {

    @Override
    public void onAction(WeiboAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_WEIBO_LOGIN:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        webView.registerHandler(Event.LOGIN_WEIBO, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                UMAuthListener umAuthListener = new UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        statusListener.end();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        JSONObject json = new JSONObject();
                        json.put("access_token", map.get("access_token"));
                        json.put("openid", map.get("uid"));
                        Logger.i("weibo login response : " + json.toJSONString());
                        statusListener.end();
                        function.onCallBack(json.toJSONString());
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        statusListener.end();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        statusListener.end();
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("umAuthListener", umAuthListener);
                control.doCommand(new WeiboLoginCommand(new WeiboLoginReceiver()), params, null);
            }
        });
    }
}
