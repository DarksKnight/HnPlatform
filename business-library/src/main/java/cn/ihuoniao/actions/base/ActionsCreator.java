package cn.ihuoniao.actions.base;

import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AppConfigAction;
import cn.ihuoniao.dispatcher.Dispatcher;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public enum  ActionsCreator {
    INSTANCE;

    public void request_getAppConfig() {
        Dispatcher.INSTANCE.dispatch(new AppConfigAction(TYPE.TYPE_APP_CONFIG, null));
    }
}
