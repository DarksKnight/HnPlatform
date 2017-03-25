package cn.ihuoniao.store;

import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.function.command.QQLoginCommand;
import cn.ihuoniao.function.receiver.QQLoginReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQStore extends Store<QQAction> {

    @Override
    public void onAction(QQAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_QQ_LOGIN:
                qqLogin();
                break;
            default:
                break;
        }
    }

    public void qqLogin() {
        webView.registerHandler(Event.LOGIN_QQ, new BridgeHandler() {
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
                        json.putAll(map);
                        Logger.i("qq login response : " + json.toJSONString());
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
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params, null);
            }
        });
    }
}
