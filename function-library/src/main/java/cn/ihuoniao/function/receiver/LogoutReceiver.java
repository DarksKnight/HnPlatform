package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.util.SPUtils;

/**
 * Created by apple on 2017/3/25.
 */

public class LogoutReceiver extends Receiver {

    public void logout(Activity activity, UMAuthListener umAuthListener) {
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.QQ, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.SINA, umAuthListener);
        SPUtils.pushString("pushPassport", "");
        SPUtils.pushString("locationPassport", "");
    }

}
