package cn.ihuoniao.store.base;

import com.squareup.otto.Bus;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.actions.base.BaseAction;
import cn.ihuoniao.event.base.StoreChangeEvent;
import cn.ihuoniao.function.command.base.Control;
import cn.ihuoniao.function.listener.StatusListener;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeWebView;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public abstract class Store<T extends BaseAction> {

    private final Bus bus = new Bus();
    protected BridgeWebView webView = null;
    protected Activity activity = null;
    protected StatusListener statusListener = null;

    protected Control control = Control.INSTANCE;

    protected Store() {
    }

    public void register(final Object view) {
        bus.register(view);
    }

    public void unregister(final Object view) {
        bus.unregister(view);
    }

    protected void emitStoreChange(StoreChangeEvent event) {
        bus.post(event);
    }

    public void onAction(T action) {
        Map<String, Object> infos = (Map<String, Object>)action.getData();
        if (null != infos) {
            webView = (BridgeWebView)infos.get("webview");
            activity = (Activity)infos.get("activity");
            statusListener = (StatusListener)infos.get("statusListener");
        }
    }
}
