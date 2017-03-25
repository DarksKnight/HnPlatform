package cn.ihuoniao.function.command;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeChatLoginReceiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatLoginCommand extends Command<Object, WeChatLoginReceiver> {

    public WeChatLoginCommand(WeChatLoginReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        IWXAPI wxapi = (IWXAPI)params.get("wxApi");
        receiver.login(wxapi);
    }
}
