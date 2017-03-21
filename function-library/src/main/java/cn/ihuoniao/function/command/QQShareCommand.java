package cn.ihuoniao.function.command;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.receiver.QQShareReceiver;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public class QQShareCommand extends Command<QQShareReceiver> {

    public QQShareCommand(QQShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public Object execute(Map<String, Object> params) {
        receiver.share();
        return null;
    }
}
