package cn.ihuoniao.actions.base;

import java.util.Map;

import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AppConfigAction;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.dispatcher.Dispatcher;
import cn.ihuoniao.store.AppConfigStore;
import cn.ihuoniao.store.QQStore;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public enum  ActionsCreator {
    INSTANCE;

    public void request_getAppConfig() {
        Dispatcher.INSTANCE.dispatch(AppConfigStore.class.getSimpleName(), new AppConfigAction(TYPE.TYPE_APP_CONFIG, null));
    }

    public void action_qqLogin(Map<String, Object> params) {
        Dispatcher.INSTANCE.dispatch(QQStore.class.getSimpleName(), new QQAction(TYPE.TYPE_QQ_LOGIN, params));
    }
}
