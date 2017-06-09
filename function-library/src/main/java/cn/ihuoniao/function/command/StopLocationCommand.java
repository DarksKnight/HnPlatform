package cn.ihuoniao.function.command;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.LocationReceiver;

/**
 * Created by sdk-app-shy on 2017/6/9.
 */

public class StopLocationCommand extends Command<Object, LocationReceiver> {

    public StopLocationCommand(LocationReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        LocationReceiver.getDefault().stopLocation();
    }
}
