package cn.ihuoniao.store;

import com.alibaba.fastjson.JSONObject;

import cn.ihuoniao.Constant;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.base.BaseAction;
import cn.ihuoniao.event.AppConfigEvent;
import cn.ihuoniao.model.AppConfigModel;
import cn.ihuoniao.request.AppConfigRequest;
import cn.ihuoniao.request.base.RequestCallBack;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppConfigStore extends Store<AppConfigEvent> {

    @Override
    public void onAction(BaseAction action) {
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
                Constant.APP_INFO.isNeedFinish = true;
                Constant.APP_INFO.platformUrl = event.appConfig.cfg_basehost;
                emitStoreChange(event);
            }

            @Override
            public void onFail(String code, String msg) {

            }
        });
    }
}
