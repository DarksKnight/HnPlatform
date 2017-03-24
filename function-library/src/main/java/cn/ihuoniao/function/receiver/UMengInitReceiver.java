package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.Map;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class UMengInitReceiver extends Receiver {

    public void init(Activity activity, Map<String, String> params) {
        PlatformConfig.setWeixin(params.get("wxAppId"), params.get("wxAppSecret"));
        PlatformConfig.setQQZone(params.get("qqAppId"), params.get("qqAppKey"));
        PlatformConfig.setSinaWeibo(params.get("weiboAkey"), params.get("weiboSkey"), "http://ihuoniao.cn");
        UMShareAPI.get(activity.getApplicationContext());
    }
}
