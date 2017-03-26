package cn.ihuoniao.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.event.AppEvent;
import cn.ihuoniao.function.command.LogoutCommand;
import cn.ihuoniao.function.command.XGRegisterCommand;
import cn.ihuoniao.function.receiver.LogoutReceiver;
import cn.ihuoniao.function.receiver.XGReceiver;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.model.AppConfigModel;
import cn.ihuoniao.model.LoginFinishModel;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.request.AppConfigRequest;
import cn.ihuoniao.request.base.RequestCallBack;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppStore extends Store<cn.ihuoniao.actions.AppAction> {

    @Override
    public void onAction(cn.ihuoniao.actions.AppAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_APP_CONFIG:
                getAppConfigRequest();
                break;
            case TYPE.TYPE_GET_APP_INFO:
                getAppInfo();
                break;
            case TYPE.TYPE_APP_LOGOUT:
                logout();
                break;
            case TYPE.TYPE_APP_LOGIN_FINISH:
                loginFinish();
                break;
            default:
                break;
        }

    }

    private void getAppConfigRequest() {
        new AppConfigRequest().request(null, new RequestCallBack() {
            @Override
            public void onSuccess(String content) {
                AppConfigModel appConfig = JSONObject.parseObject(content, AppConfigModel.class);
                AppEvent event = new AppEvent();
                event.appConfig = appConfig;
                emitStoreChange(event);
            }

            @Override
            public void onFail(String code, String msg) {

            }
        });
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

    private void logout() {
        webView.registerHandler(Event.APP_LOGOUT, new BridgeHandler() {
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
                        statusListener.end();
                        function.onCallBack("");
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
                control.doCommand(new LogoutCommand(new LogoutReceiver()), params, null);
            }
        });
    }

    private void loginFinish() {
        webView.registerHandler(Event.APP_LOGIN_FINISH, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LoginFinishModel loginFinishInfo = JSON.parseObject(data, LoginFinishModel.class);
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("passport", loginFinishInfo.passport);
                control.doCommand(new XGRegisterCommand(new XGReceiver()), params, null);
            }
        });
    }
}
