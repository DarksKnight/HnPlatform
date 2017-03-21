package cn.ihuoniao.function.command;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.receiver.QQLoginReceiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQLoginCommand extends Command<QQLoginReceiver> {

    public QQLoginCommand(QQLoginReceiver receiver) {
        super(receiver);
    }

    @Override
    public Object execute(Map<String, Object> params) {
        Activity activity = (Activity)params.get("activity");
        Tencent tencent = (Tencent)params.get("tencent");
        IUiListener listener = (IUiListener)params.get("listener");
        receiver.login(tencent, activity, listener);
        return null;
    }
}
