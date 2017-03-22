package cn.ihuoniao.store;

import com.alibaba.fastjson.JSONObject;

import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AppConfigAction;
import cn.ihuoniao.event.AppConfigEvent;
import cn.ihuoniao.model.AppConfigModel;
import cn.ihuoniao.request.AppConfigRequest;
import cn.ihuoniao.request.base.RequestCallBack;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppConfigStore extends Store<AppConfigAction> {

    @Override
    public void onAction(AppConfigAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_APP_CONFIG:
                getAppConfigRequest();
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
                AppConfigEvent event = new AppConfigEvent();
                event.appConfig = appConfig;
                emitStoreChange(event);
            }

            @Override
            public void onFail(String code, String msg) {

            }
        });
    }
}
