package cn.ihuoniao.receiver;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;

import cn.ihuoniao.activity.MainActivity;

/**
 * Created by sdk-app-shy on 2017/6/13.
 */

public class HnMsgReceiver extends MessageReceiver {

    @Override
    protected void onNotificationOpened(Context context, String s, String s1, String s2) {
        super.onNotificationOpened(context, s, s1, s2);
        String url = JSON.parseObject(s2).getString("url");
        if (url.trim().length() > 0) {
            MainActivity.isLoadMainWeb = false;
            MainActivity.bwvContent.loadUrl(url);
        }
    }
}
