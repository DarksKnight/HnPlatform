package cn.ihuoniao.function.receiver;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.app.Activity;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by apple on 2017/3/25.
 */

public class LogoutReceiver extends Receiver {

    public void logout(Activity activity, UMAuthListener umAuthListener) {
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.QQ, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.SINA, umAuthListener);
    }

}
