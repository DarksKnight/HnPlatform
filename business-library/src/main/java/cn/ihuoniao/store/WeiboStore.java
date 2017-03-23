package cn.ihuoniao.store;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.WeiboAction;
import cn.ihuoniao.event.WeiboEvent;
import cn.ihuoniao.function.command.WeiboInitCommand;
import cn.ihuoniao.function.command.WeiboLoginCommand;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeiboInitReceiver;
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
        Map<String, Object> infos = action.getData();
        switch (action.getType()) {
            case TYPE.TYPE_WEIBO_INIT:
                init(infos.get("weiboAkey").toString());
                break;
            case TYPE.TYPE_WEIBO_LOGIN:
                login((SsoHandler)infos.get("weiboHandler"));
                break;
            default:
                break;
        }
    }

    private void init(String appKey) {
        Map<String, Object> params = new HashMap<>();
        params.put("context", activity);
        params.put("weiboAkey", appKey);
        control.doCommand(new WeiboInitCommand(new WeiboInitReceiver()), params, new ResultListener<SsoHandler>() {
            @Override
            public void onResult(SsoHandler result) {
                WeiboEvent event = new WeiboEvent();
                event.eventName = Event.INIT_WEIBO;
                event.handler = result;
                emitStoreChange(event);
            }
        });
    }

    private void login(final SsoHandler handler) {
        webView.registerHandler(Event.LOGIN_WEIBO, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                WeiboAuthListener listener = new WeiboAuthListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        Oauth2AccessToken info = Oauth2AccessToken.parseAccessToken(bundle);
                        JSONObject json = new JSONObject();
                        json.put("phoneNumber", info.getPhoneNum());
                        json.put("refreshToken", info.getRefreshToken());
                        json.put("expiresTime", info.getExpiresTime());
                        json.put("token", info.getToken());
                        json.put("uid", info.getUid());
                        statusListener.end();
                        Logger.i("weibo login response : " + json.toJSONString());
                        function.onCallBack(json.toJSONString());
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        statusListener.end();
                    }

                    @Override
                    public void onCancel() {
                        statusListener.end();
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("weiboHandler", handler);
                params.put("weiboAuthListener", listener);
                control.doCommand(new WeiboLoginCommand(new WeiboLoginReceiver()), params, null);
            }
        });
    }
}
