package cn.ihuoniao.store;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.WeChatAction;
import cn.ihuoniao.event.WeChatEvent;
import cn.ihuoniao.function.command.WeChatInitCommand;
import cn.ihuoniao.function.command.WeChatLoginCommand;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeChatInitReceiver;
import cn.ihuoniao.function.receiver.WeChatLoginReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.request.WeChatLoginRequest;
import cn.ihuoniao.request.base.RequestCallBack;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatStore extends Store<WeChatAction> {

    @Override
    public void onAction(WeChatAction action) {
        super.onAction(action);
        Map<String, Object> infos = action.getData();
        switch (action.getType()) {
            case TYPE.TYPE_WECHAT_INIT:
                init(infos.get("wechatAppId").toString());
                break;
            case TYPE.TYPE_WECHAT_LOGIN:
                wechatLogin((IWXAPI)infos.get("wxApi"));
                break;
            case TYPE.TYPE_LOGIN_WECHAT_INFO:
                SendAuth.Resp response = (SendAuth.Resp)infos.get("response");
                String appid = infos.get("wechatAppId").toString();
                String secret = infos.get("wechatSecret").toString();
                getWeChatLoginInfo(response, appid, secret);
                break;
            default:
                break;
        }
    }

    private void init(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("context", activity);
        params.put("appId", appId);
        control.doCommand(new WeChatInitCommand(new WeChatInitReceiver()), params, new ResultListener<IWXAPI>() {

            @Override
            public void onResult(IWXAPI result) {
                WeChatEvent event = new WeChatEvent();
                event.eventName = Event.INIT_WECHAT;
                event.wxApi = result;
                emitStoreChange(event);
            }
        });
    }

    private void wechatLogin(final IWXAPI wxApi) {
        webView.registerHandler(Event.LOGIN_WECHAT, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                Map<String, Object> params = new HashMap<>();
                params.put("wxApi", wxApi);
                params.put("activity", activity);
                WeChatEvent event = new WeChatEvent();
                event.eventName = Event.GET_LOGIN_WECHAT_INFO;
                event.function = function;
                emitStoreChange(event);
                control.doCommand(new WeChatLoginCommand(new WeChatLoginReceiver()), params, null);
            }
        });
    }

    private void getWeChatLoginInfo(SendAuth.Resp response, String appid, String secret) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("code", response.code);
        params.put("grant_type", "authorization_code");
        new WeChatLoginRequest().request(params, new RequestCallBack() {
            @Override
            public void onSuccess(String content) {
                Logger.i("wx login response : " + content);
                statusListener.end();
                WeChatEvent event = new WeChatEvent();
                event.eventName = Event.GET_LOGIN_WECHAT_INFO;
                event.wxLoginInfo = content;
                emitStoreChange(event);
            }

            @Override
            public void onFail(String code, String msg) {

            }
        });
    }
}
