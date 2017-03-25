package cn.ihuoniao.function.command;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeChatInitReceiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatInitCommand extends Command<IWXAPI, WeChatInitReceiver> {

    public WeChatInitCommand(WeChatInitReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<IWXAPI> listener) {
        String appId = params.get("appId").toString();
        Context context = (Context)params.get("context");
        listener.onResult(receiver.init(context, appId));
    }
}
