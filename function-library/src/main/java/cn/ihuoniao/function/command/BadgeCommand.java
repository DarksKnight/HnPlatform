package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.BadgeReceiver;

/**
 * Created by sdk-app-shy on 2017/4/13.
 */

public class BadgeCommand extends Command<Object, BadgeReceiver> {

    public BadgeCommand(BadgeReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        String badgeCount = params.get("badgeCount").toString();
        receiver.updateBadgeValue(activity, badgeCount);
    }
}
