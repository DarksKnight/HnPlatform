package cn.ihuoniao.event;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import cn.ihuoniao.event.base.StoreChangeEvent;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboEvent extends StoreChangeEvent {

    public SsoHandler handler = null;
}
