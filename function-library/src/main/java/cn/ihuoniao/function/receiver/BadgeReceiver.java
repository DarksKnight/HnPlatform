package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/4/13.
 */

public class BadgeReceiver extends Receiver {

    public void updateBadgeValue(Activity activity, String badgeCount) {
        try {
            Badges.setBadge(activity, Integer.parseInt(badgeCount));
        } catch (BadgesNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
