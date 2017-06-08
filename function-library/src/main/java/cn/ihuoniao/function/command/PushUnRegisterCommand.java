package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.PushUnRegisterReceiver;

/**
 * Created by sdk-app-shy on 2017/6/8.
 */

public class PushUnRegisterCommand extends Command<Object, PushUnRegisterReceiver> {

    public PushUnRegisterCommand(PushUnRegisterReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        String passport = params.get("passport").toString();
        receiver.unregister(activity, passport, listener);
    }
}
