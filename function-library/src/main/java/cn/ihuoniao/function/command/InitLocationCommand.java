package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.LocationReceiver;

/**
 * Created by sdk-app-shy on 2017/6/9.
 */

public class InitLocationCommand extends Command<Object,LocationReceiver> {

    public InitLocationCommand(LocationReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        int second = 30 * 1000;
        LocationReceiver.getDefault().init(activity, second);
    }
}
