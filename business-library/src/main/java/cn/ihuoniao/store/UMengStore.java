package cn.ihuoniao.store;

import com.alibaba.fastjson.JSON;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.UMengAction;
import cn.ihuoniao.function.command.UMengInitCommand;
import cn.ihuoniao.function.command.UMengShareCommand;
import cn.ihuoniao.function.receiver.UMengInitReceiver;
import cn.ihuoniao.function.receiver.UMengShareReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.model.ShareInfoModel;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class UMengStore extends Store<UMengAction> {

    @Override
    public void onAction(UMengAction action) {
        super.onAction(action);
        Map<String, Object> infos = action.getData();
        switch (action.getType()) {
            case TYPE.TYPE_UMENG_INIT:
                init(infos);
                break;
            case TYPE.TYPE_UMENG_SHARE:
                share();
                break;
            default:
                break;
        }
    }

    private void init(Map<String, Object> infos) {
        Map<String, Object> params = new HashMap<>();
        params.put("context", activity);
        params.put("wxAppId", infos.get("wechatAppId"));
        params.put("wxAppSecret", infos.get("wechatSecret"));
        params.put("qqAppId", infos.get("qqAppId"));
        params.put("qqAppKey", infos.get("qqAppKey"));
        params.put("weiboAkey", infos.get("weiboAkey"));
        params.put("weiboSkey", infos.get("weiboSkey"));
        control.doCommand(new UMengInitCommand(new UMengInitReceiver()), params, null);
    }

    private void share() {
        webView.registerHandler(Event.SHARE_UMENG, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                ShareInfoModel shareInfo = JSON.parseObject(data, ShareInfoModel.class);
                UMShareListener listener = new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        statusListener.end();
                        Logger.i("umeng share result end");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        statusListener.end();
                        Logger.i("umeng share result error");
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        statusListener.end();
                        Logger.i("umeng share result cancel");
                    }
                };
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("umengShareListener", listener);
                params.put("umengShareTitle", shareInfo.title);
                params.put("umengShareSummary", shareInfo.summary);
                params.put("umengShareUrl", shareInfo.url);
                params.put("umengShareImageUrl", shareInfo.imageUrl);
                control.doCommand(new UMengShareCommand(new UMengShareReceiver()), params ,null);
            }
        });
    }
}
