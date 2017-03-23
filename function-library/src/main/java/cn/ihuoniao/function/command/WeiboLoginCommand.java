package cn.ihuoniao.function.command;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeiboLoginReceiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboLoginCommand extends Command<Object, WeiboLoginReceiver> {

    public WeiboLoginCommand(WeiboLoginReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, final ResultListener<Object> listener) {
        SsoHandler handler = (SsoHandler)params.get("weiboHandler");
        WeiboAuthListener weiboAuthListener = (WeiboAuthListener)params.get("weiboAuthListener");
        receiver.login(handler, weiboAuthListener);
    }
}
