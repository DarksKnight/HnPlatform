package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboInitReceiver extends Receiver {

    public SsoHandler init(Activity activity, String appKey) {
        String scope =
                "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";
        AuthInfo authInfo = new AuthInfo(activity, appKey, "https://api.weibo.com/oauth2/default.html", scope);
        return new SsoHandler(activity, authInfo);
    }
}
