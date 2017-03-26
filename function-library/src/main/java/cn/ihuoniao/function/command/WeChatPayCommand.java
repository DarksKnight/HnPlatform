package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.WeChatPayReceiver;

/**
 * Created by apple on 2017/3/26.
 */

public class WeChatPayCommand extends Command<String, WeChatPayReceiver> {

    public WeChatPayCommand(WeChatPayReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<String> listener) {
        Activity activity = (Activity)params.get("activity");
        String appId = params.get("appId").toString();
        String partnerId = params.get("partnerId").toString();
        String prepayId = params.get("prepayId").toString();
        String nonceStr = params.get("nonceStr").toString();
        String timeStamp = params.get("timeStamp").toString();
        String sign = params.get("sign").toString();
        receiver.pay(activity, appId, partnerId, prepayId, nonceStr, timeStamp, sign);
    }
}
