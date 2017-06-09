package cn.ihuoniao.store;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Constant;
import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.event.AppEvent;
import cn.ihuoniao.function.command.BadgeCommand;
import cn.ihuoniao.function.command.InitLocationCommand;
import cn.ihuoniao.function.command.LogoutCommand;
import cn.ihuoniao.function.command.PushUnRegisterCommand;
import cn.ihuoniao.function.command.StopLocationCommand;
import cn.ihuoniao.function.command.UploadLocationCommand;
import cn.ihuoniao.function.command.XGRegisterCommand;
import cn.ihuoniao.function.command.XGUnRegisterCommand;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.BadgeReceiver;
import cn.ihuoniao.function.receiver.LocationReceiver;
import cn.ihuoniao.function.receiver.LogoutReceiver;
import cn.ihuoniao.function.receiver.PushUnRegisterReceiver;
import cn.ihuoniao.function.receiver.XGReceiver;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.function.util.SPUtils;
import cn.ihuoniao.model.AppConfigModel;
import cn.ihuoniao.model.AppInfoModel;
import cn.ihuoniao.model.LoginFinishModel;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.request.AppConfigRequest;
import cn.ihuoniao.request.base.RequestCallBack;
import cn.ihuoniao.store.base.Store;
import io.github.xudaojie.qrcodelib.CaptureActivity;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppStore extends Store<cn.ihuoniao.actions.AppAction> {

    @Override
    public void onAction(cn.ihuoniao.actions.AppAction action) {
        super.onAction(action);
        Map<String, Object> infos = (Map<String, Object>)action.getData();
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
            case TYPE.TYPE_UPDATE_APP_BADGE_VALUE:
                updateBadge();
                break;
            case TYPE.TYPE_GET_PUSH_STATUS:
                getPushStatus();
                break;
            case TYPE.TYPE_SET_PUSH_STATUS:
                setPushStatus();
                break;
            case TYPE.TYPE_GET_CACHE_SIZE:
                getCacheSize();
                break;
            case TYPE.TYPE_CLEAR_CACHE:
                clearCache();
                break;
            case TYPE.TYPE_SHOW_NAVIGATIONBAR:
                showNavigationBar();
                break;
            case TYPE.TYPE_HIDE_NAVIGATIONBAR:
                hideNavigationBar();
                break;
            case TYPE.TYPE_SHOW_QRCODE_SCAN:
                showQRCodeScan((ResultListener<CallBackFunction>)infos.get("listener"));
                break;
            case TYPE.TYPE_SET_DRAG_REFRESH:
                setDragRefresh((ResultListener<String>)infos.get("listener"));
                break;
            case TYPE.TYPE_INIT_LOCATION:
                initLocation();
                break;
            default:
                break;
        }

    }

    private void getAppConfigRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("activity", activity);
        params.put("passport", "");
        control.doCommand(new XGRegisterCommand(new XGReceiver()), params, null);
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
                json.put("pushToken", AppInfoModel.INSTANCE.pushToken);
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
                params.put("passport", SPUtils.getString("pushPassport"));
                control.doCommand(new PushUnRegisterCommand(new PushUnRegisterReceiver()), params, null);
                control.doCommand(new LogoutCommand(new LogoutReceiver()), params, null);
                stopLocation();
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
                uploadLocation(loginFinishInfo.passport);
                control.doCommand(new XGRegisterCommand(new XGReceiver()), params, new ResultListener() {
                    @Override
                    public void onResult(Object result) {
                        boolean flag = (boolean)result;
                        if (flag) {
                            AppInfoModel.INSTANCE.pushStatus = "on";
                        }
                    }
                });
            }
        });
    }

    private void getPushStatus() {
        webView.registerHandler(Event.GET_PUSH_STATUS, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(AppInfoModel.INSTANCE.pushStatus);
            }
        });
    }

    private void setPushStatus() {
        webView.registerHandler(Event.SET_PUSH_STATUS, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                String status = JSON.parseObject(data).getString("pushStatus");
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("passport", "");
                if (status.equals("on")) {
                    control.doCommand(new XGRegisterCommand(new XGReceiver()), params, new ResultListener() {
                        @Override
                        public void onResult(Object result) {
                            boolean flag = (boolean)result;
                            if (flag) {
                                AppInfoModel.INSTANCE.pushStatus = "on";
                            }
                            function.onCallBack(AppInfoModel.INSTANCE.pushStatus);
                        }
                    });
                } else {
                    control.doCommand(new XGUnRegisterCommand(new XGReceiver()), params, new ResultListener() {
                        @Override
                        public void onResult(Object result) {
                            AppInfoModel.INSTANCE.pushStatus = "off";
                            function.onCallBack(AppInfoModel.INSTANCE.pushStatus);
                        }
                    });
                }
            }
        });
    }

    private void updateBadge() {
        webView.registerHandler(Event.UPDATE_APP_BADGE_VALUE, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String badgeCount = JSON.parseObject(data).getString("badge");
                Logger.i("data : " + data);
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("badgeCount", badgeCount);
                control.doCommand(new BadgeCommand(new BadgeReceiver()), params, null);
                function.onCallBack("success");
            }
        });
    }

    private void getCacheSize() {
        webView.registerHandler(Event.GET_CACHE_SIZE, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("cache size : " + CommonUtil.getTotalCacheSize(activity));
            }
        });
    }

    private void clearCache() {
        webView.registerHandler(Event.CLEAR_CACHE, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                CommonUtil.clearCache(activity);
                function.onCallBack("缓存清理成功");
            }
        });
    }

    private void showNavigationBar() {
        webView.callHandler(Event.SHOW_NAVIGATIONBAR, "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }

    private void hideNavigationBar() {
        webView.callHandler(Event.HIDE_NAVIGATIONBAR, "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }

    private void showQRCodeScan(final ResultListener<CallBackFunction> listener) {
        webView.registerHandler(Event.SHOW_QRCODE_SCAN, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                listener.onResult(function);
                Intent intent = new Intent(activity, CaptureActivity.class);
                activity.startActivityForResult(intent, Constant.CODE_SCAN_RESULT);
            }
        });
    }

    private void setDragRefresh(final ResultListener<String> listener) {
        webView.registerHandler(Event.SET_DRAG_REFRESH, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String status = JSON.parseObject(data).getString("value");
                listener.onResult(status);
            }
        });
    }

    private void initLocation() {
        Map<String, Object> params = new HashMap<>();
        params.put("activity", activity);
        control.doCommand(new InitLocationCommand(LocationReceiver.getDefault()), params, null);
    }

    private void uploadLocation(String passport) {
        SPUtils.pushString("locationPassport", passport);
        control.doCommand(new UploadLocationCommand(LocationReceiver.getDefault()), null ,null);
    }

    private void stopLocation() {
        control.doCommand(new StopLocationCommand(LocationReceiver.getDefault()), null, null);
    }
}
