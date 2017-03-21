package cn.ihuoniao.function.command;

import android.content.Context;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.receiver.QQInitReceiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQInitCommand extends Command<QQInitReceiver> {

    public QQInitCommand(QQInitReceiver receiver) {
        super(receiver);
    }

    @Override
    public Object execute(Map<String, Object> params) {
        String appId = params.get("appId").toString();
        Context context = (Context)params.get("context");
        return receiver.init(context, appId);
    }
}
