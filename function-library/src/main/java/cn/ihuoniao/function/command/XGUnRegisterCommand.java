package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.XGReceiver;

/**
 * Created by sdk-app-shy on 2017/4/13.
 */

public class XGUnRegisterCommand extends Command<Object, XGReceiver> {

    public XGUnRegisterCommand(XGReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        receiver.unregister(activity, listener);
    }
}
