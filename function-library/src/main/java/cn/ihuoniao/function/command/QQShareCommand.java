package cn.ihuoniao.function.command;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public class QQShareCommand extends Command<QQShareReceiver> {

    public QQShareCommand(QQShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params) {
        receiver.share();
    }
}
