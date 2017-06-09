package cn.ihuoniao.function.command;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.LocationReceiver;

/**
 * Created by sdk-app-shy on 2017/6/9.
 */

public class UploadLocationCommand extends Command<Object, LocationReceiver> {

    public UploadLocationCommand(LocationReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Timer timer = new Timer();
        final int second = 30 * 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocationReceiver.getDefault().uploadLocation();
            }
        }, 0, second);
    }
}
