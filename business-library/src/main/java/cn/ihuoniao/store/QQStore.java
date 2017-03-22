package cn.ihuoniao.store;

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
import cn.ihuoniao.function.receiver.QQInitReceiver;
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
        Map<String, Object> infos = action.getData();
        switch (action.getType()) {
            case TYPE.TYPE_QQ_INIT:
                init(infos.get("appId").toString());
                break;
            case TYPE.TYPE_QQ_LOGIN:
                qqLogin((Tencent)infos.get("tencent"));
                break;
            default:
                break;
        }
    }

    private void init(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("context", activity);
        params.put("appId", appId);
        Tencent tencent = (Tencent) control.doCommand(new QQInitCommand(new QQInitReceiver()), params);
        QQEvent event = new QQEvent();
        event.eventName = Event.QQ_INIT;
        event.tencent = tencent;
        emitStoreChange(event);
    }

    public void qqLogin(final Tencent tencent) {
        webView.registerHandler(Event.QQ_LOGIN, new BridgeHandler() {
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

                    }

                    @Override
                    public void onCancel() {

                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", activity);
                params.put("listener", listener);
                QQEvent event = new QQEvent();
                event.eventName = Event.QQ_LOGIN;
                event.listener = listener;
                emitStoreChange(event);
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params);
            }
        });
    }
}
