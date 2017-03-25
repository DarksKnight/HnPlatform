package cn.ihuoniao.function.receiver;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.app.Activity;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeiboLoginReceiver extends Receiver {

    public void login(Activity activity, UMAuthListener umAuthListener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.SINA, umAuthListener);
    }
}
