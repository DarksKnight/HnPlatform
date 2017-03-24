package cn.ihuoniao.store;

import com.alibaba.fastjson.JSON;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.event.QQEvent;
import cn.ihuoniao.function.command.QQInitCommand;
import cn.ihuoniao.function.command.QQLoginCommand;
import cn.ihuoniao.function.command.QQShareCommand;
import cn.ihuoniao.function.command.QQZoneShareCommand;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.QQInitReceiver;
import cn.ihuoniao.function.receiver.QQLoginReceiver;
import cn.ihuoniao.function.receiver.QQShareReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.model.ShareInfoModel;
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
        Map<String, Object> infos = action.getData();
        switch (action.getType()) {
            case TYPE.TYPE_QQ_INIT:
                init(infos.get("qqAppId").toString());
                break;
            case TYPE.TYPE_QQ_LOGIN:
                qqLogin((Tencent)infos.get("tencent"));
                break;
            case TYPE.TYPE_QQ_ZONE_SHARE:
                qqZoneShare((Tencent)infos.get("tencent"));
                break;
            case TYPE.TYPE_QQ_SHARE:
                qqShare((Tencent)infos.get("tencent"));
                break;
            default:
                break;
        }
    }

    private void init(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("context", activity);
        params.put("appId", appId);
        control.doCommand(new QQInitCommand(new QQInitReceiver()), params, new ResultListener<Tencent>() {
            @Override
            public void onResult(Tencent result) {
                QQEvent event = new QQEvent();
                event.eventName = Event.INIT_QQ;
                event.tencent = result;
                emitStoreChange(event);
            }
        });

    }

    public void qqLogin(final Tencent tencent) {
        webView.registerHandler(Event.LOGIN_QQ, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Logger.i("qq login response : " + o.toString());
                        statusListener.end();
                        function.onCallBack(o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        statusListener.end();
                    }

                    @Override
                    public void onCancel() {
                        statusListener.end();
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", activity);
                params.put("listener", listener);
                QQEvent event = new QQEvent();
                event.eventName = Event.LOGIN_QQ;
                event.listener = listener;
                emitStoreChange(event);
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params, null);
            }
        });
    }

    private void qqShare(final Tencent tencent) {
        webView.registerHandler(Event.SHARE_QQ, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                ShareInfoModel shareInfo = JSON.parseObject(data, ShareInfoModel.class);
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Logger.i("qq share response : " + o.toString());
                        statusListener.end();
                        function.onCallBack(o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        statusListener.end();
                    }

                    @Override
                    public void onCancel() {
                        statusListener.end();
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", activity);
                params.put("listener", listener);
                params.put("qqTitle", shareInfo.title);
                params.put("qqSummary", shareInfo.summary);
                params.put("qqUrl", shareInfo.url);
                params.put("qqImageUrl", shareInfo.imageUrl);
                QQEvent event = new QQEvent();
                event.eventName = Event.SHARE_QQ;
                event.listener = listener;
                emitStoreChange(event);
                control.doCommand(new QQShareCommand(new QQShareReceiver()), params, null);
            }
        });
    }

    private void qqZoneShare(final Tencent tencent) {
        webView.registerHandler(Event.SHARE_QQ_ZONE, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                ShareInfoModel shareInfo = JSON.parseObject(data, ShareInfoModel.class);
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Logger.i("qq zone share response : " + o.toString());
                        statusListener.end();
                        function.onCallBack(o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        statusListener.end();
                    }

                    @Override
                    public void onCancel() {
                        statusListener.end();
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", activity);
                params.put("listener", listener);
                params.put("qqZoneTitle", shareInfo.title);
                params.put("qqZoneSummary", shareInfo.summary);
                params.put("qqZoneUrl", shareInfo.url);
                params.put("imageUrls", shareInfo.imageUrls);
                QQEvent event = new QQEvent();
                event.eventName = Event.SHARE_QQ_ZONE;
                event.listener = listener;
                emitStoreChange(event);
                control.doCommand(new QQZoneShareCommand(new QQShareReceiver()), params, null);
            }
        });
    }
}
