package cn.ihuoniao.function.command;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.QQShareReceiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQZoneShareCommand extends Command<Object, QQShareReceiver> {

    public QQZoneShareCommand(QQShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        receiver.shareQQZone();
    }
}
