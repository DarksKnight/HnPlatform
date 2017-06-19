package cn.ihuoniao.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Vibrator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

import cn.ihuoniao.activity.MainActivity;

/**
 * Created by sdk-app-shy on 2017/6/13.
 */

public class HnMsgReceiver extends MessageReceiver {

    @Override
    protected void onNotification(Context context, String s, String s1, Map<String, String> map) {
        super.onNotification(context, s, s1, map);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 1000, 1000, 1000, 1000, 1000};
        vibrator.vibrate(pattern, -1);
        NotificationManager localNotificationManager = (NotificationManager)context.getSystemService("notification");
        localNotificationManager.notify();
//        String music = map.get("music");
//        int soundId = 0;
//        if (null != music && music.trim().length() > 0) {
//            soundId = context.getResources().getIdentifier(music, "raw", context.getPackageName());
//        } else {
//            soundId = context.getResources().getIdentifier("alicloud_notification_sound", "raw", context.getPackageName());
//        }
//        String path = "android.resource://" + context.getPackageName() + "/" + soundId;
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.setNotificationSoundFilePath(path);
    }

    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        super.onMessage(context, cPushMessage);
        String content = cPushMessage.getContent();
        JSONObject jsonObject = JSON.parseObject(content);
        String music = jsonObject.getString()
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 1000, 1000, 1000, 1000, 1000};
        vibrator.vibrate(pattern, -1);
    }

    @Override
    protected void onNotificationOpened(Context context, String s, String s1, String s2) {
        super.onNotificationOpened(context, s, s1, s2);
        String url = JSON.parseObject(s2).getString("url");
        if (url.trim().length() > 0) {
            MainActivity.isLoadMainWeb = false;
            MainActivity.bwvContent.loadUrl(url);
        }
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String s, String s1, Map<String, String> map, int i, String s2, String s3) {
        super.onNotificationReceivedInApp(context, s, s1, map, i, s2, s3);
    }
}
