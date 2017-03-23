package cn.ihuoniao.function.command;

import android.content.Context;

import com.tencent.tauth.Tencent;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.QQInitReceiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQInitCommand extends Command<Tencent, QQInitReceiver> {

    public QQInitCommand(QQInitReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Tencent> listener) {
        String appId = params.get("appId").toString();
        Context context = (Context)params.get("context");
        listener.onResult(receiver.init(context, appId));
    }
}
