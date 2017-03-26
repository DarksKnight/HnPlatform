package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.XGReceiver;

/**
 * Created by apple on 2017/3/26.
 */

public class XGRegisterCommand extends Command<Object, XGReceiver> {

    public XGRegisterCommand(XGReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        String passport = params.get("passport").toString();
        receiver.register(activity, passport);
    }
}
