package cn.ihuoniao.function.command;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.XGReceiver;

/**
 * Created by sdk-app-shy on 2017/4/12.
 */

public class XGUpdateBadgeCommand extends Command<Object, XGReceiver> {
    public XGUpdateBadgeCommand(XGReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        receiver.updateBadgeValue();
    }
}
