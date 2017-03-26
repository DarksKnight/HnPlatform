package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.AlipayReceiver;

/**
 * Created by apple on 2017/3/26.
 */

public class AlipayCommand extends Command<String, AlipayReceiver> {

    public AlipayCommand(AlipayReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<String> listener) {
        Activity activity = (Activity)params.get("activity");
        String orderInfo = params.get("orderInfo").toString();
        receiver.pay(activity, orderInfo, listener);
    }
}
