package cn.ihuoniao.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.WeChatAction;
import cn.ihuoniao.function.command.WeChatLoginCommand;
import cn.ihuoniao.function.command.WeChatPayCommand;
import cn.ihuoniao.function.receiver.WeChatLoginReceiver;
import cn.ihuoniao.function.receiver.WeChatPayReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.model.PayInfoModel;
import cn.ihuoniao.model.WechatPayInfoModel;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
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
            case TYPE.TYPE_WECHAT_LOGIN:
                wechatLogin();
                break;
            case TYPE.TYPE_WECHAT_PAY:
                wechatPay(infos.get("wechatAppId").toString());
                break;
            default:
                break;
        }
    }

    private void wechatLogin() {
        webView.registerHandler(Event.LOGIN_WECHAT, new BridgeHandler() {
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
                        Logger.i("wechat login response : " + json.toJSONString());
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
                control.doCommand(new WeChatLoginCommand(new WeChatLoginReceiver()), params, null);
            }
        });
    }

    private void wechatPay(final String wxAppId) {
        webView.registerHandler(Event.PAY_WECHAT, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                Logger.i("wechat pay data : " + data);
                WechatPayInfoModel wechatPayInfo = JSON.parseObject(data, WechatPayInfoModel.class);
                PayInfoModel payInfo = wechatPayInfo.orderInfo;
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("appId", wxAppId);
                params.put("partnerId", payInfo.partnerId);
                params.put("prepayId", payInfo.prepayId);
                params.put("nonceStr", payInfo.nonceStr);
                params.put("timeStamp", payInfo.timeStamp);
                params.put("sign", payInfo.sign);
                control.doCommand(new WeChatPayCommand(new WeChatPayReceiver()), params, null);
            }
        });
    }
}
