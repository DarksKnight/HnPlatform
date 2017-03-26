package cn.ihuoniao.actions.base;

import java.util.Map;

import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AlipayAction;
import cn.ihuoniao.actions.AppAction;
import cn.ihuoniao.actions.QQAction;
import cn.ihuoniao.actions.UMengAction;
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
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_APP, new AppAction(TYPE.TYPE_APP_CONFIG, null));
    }

    public void register_getAppInfo() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_APP, new AppAction(TYPE.TYPE_GET_APP_INFO, params));
    }

    public void register_appLogout() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_APP, new AppAction(TYPE.TYPE_APP_LOGOUT, params));
    }

    public void register_qqLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_QQ, new QQAction(TYPE.TYPE_QQ_LOGIN, params));
    }

    public void register_wechatLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_WECHAT, new WeChatAction(TYPE.TYPE_WECHAT_LOGIN, params));
    }

    public void register_weiboLogin() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STROE_WEIBO, new WeiboAction(TYPE.TYPE_WEIBO_LOGIN, params));
    }

    public void init_umeng() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_UMENG, new UMengAction(TYPE.TYPE_UMENG_INIT, params));
    }

    public void register_umengShare() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_UMENG, new UMengAction(TYPE.TYPE_UMENG_SHARE, params));
    }

    public void register_alipay() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_ALIPAY, new AlipayAction(TYPE.TYPE_ALIPAY_PAY, params));
    }

    public void register_wechatPay() {
        Dispatcher.INSTANCE.dispatch(TYPE.REGISTER_STORE_WECHAT, new WeChatAction(TYPE.TYPE_WECHAT_PAY, params));
    }
}
