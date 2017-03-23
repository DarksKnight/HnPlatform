package cn.ihuoniao.function.command;

import android.app.Activity;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeiboInitReceiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboInitCommand extends Command<SsoHandler, WeiboInitReceiver> {

    public WeiboInitCommand(WeiboInitReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<SsoHandler> listener) {
        Activity activity = (Activity)params.get("context");
        String appkey = params.get("weiboAkey").toString();
        listener.onResult(receiver.init(activity, appkey));
    }
}
