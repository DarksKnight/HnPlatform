package cn.ihuoniao.store;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.function.command.QQLoginCommand;
import cn.ihuoniao.function.receiver.QQLoginReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.BridgeWebView;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQStore extends Store<QQAction> {

    @Override
    public void onAction(QQAction action) {
        switch (action.getType()) {
            case TYPE.TYPE_QQ_LOGIN:
                Map<String, Object> infos = action.getData();
                qqLogin((BridgeWebView)infos.get("webview"), (Activity)infos.get("activity"), (Tencent)infos.get("tencent"));
                break;
            default:
                break;
        }
    }

    private void qqLogin(BridgeWebView webView, final Activity activity, final Tencent tencent) {
        webView.registerHandler(Event.QQ_LOGIN, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", activity);
                params.put("listener", new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Logger.i("qq login : " + o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params);
            }
        });
    }
}
