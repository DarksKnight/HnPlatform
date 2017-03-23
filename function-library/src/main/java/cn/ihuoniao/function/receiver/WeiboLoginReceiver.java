package cn.ihuoniao.function.receiver;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboLoginReceiver extends Receiver {

    public void login(SsoHandler handler, WeiboAuthListener listener) {
        handler.authorize(listener);
    }
}
