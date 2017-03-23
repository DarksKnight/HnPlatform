package cn.ihuoniao.actions.base;

import java.util.Map;

import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AppConfigAction;
import cn.ihuoniao.actions.AppInfoAction;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.actions.WeChatAction;
import cn.ihuoniao.actions.WeiboAction;
import cn.ihuoniao.dispatcher.Dispatcher;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public enum  ActionsCreator {
    INSTANCE;

    private Map<String, Object> params = null;

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void request_getAppConfig() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_APP_CONFIG, new AppConfigAction(TYPE.TYPE_APP_CONFIG, null));
    }

    public void register_getAppInfo() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_GET_APP_INFO, new AppInfoAction(TYPE.TYPE_GET_APP_INFO, params));
    }

    public void init_qq() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_QQ, new QQAction(TYPE.TYPE_QQ_INIT, params));
    }

    public void register_qqLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_QQ, new QQAction(TYPE.TYPE_QQ_LOGIN, params));
    }

    public void init_wechat() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_WECHAT, new WeChatAction(TYPE.TYPE_WECHAT_INIT, params));
    }

    public void register_wechatLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_WECHAT, new WeChatAction(TYPE.TYPE_WECHAT_LOGIN, params));
    }

    public void request_getWeChatLoginInfo() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_WECHAT, new WeChatAction(TYPE.TYPE_LOGIN_WECHAT_INFO, params));
    }

    public void init_weibo() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STROE_WEIBO, new WeiboAction(TYPE.TYPE_WEIBO_INIT, params));
    }

    public void register_weiboLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STROE_WEIBO, new WeiboAction(TYPE.TYPE_WEIBO_LOGIN, params));
    }
}
