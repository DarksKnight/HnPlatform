package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.listener.ResultListener;

/**
 * Created by sdk-app-shy on 2017/6/8.
 */

public class PushUnRegisterReceiver extends Receiver {

    public void unregister(Activity activity, String passport, ResultListener listener) {
//        PushAgent mPushAgent = PushAgent.getInstance(activity);
//        mPushAgent.removeAlias(passport, "userID", new UTrack.ICallBack() {
//            @Override
//            public void onMessage(boolean b, String s) {
//
//            }
//        });
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.removeAlias(passport, new CommonCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailed(String s, String s1) {

            }
        });
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailed(String s, String s1) {

            }
        });
        if (null != listener) {
            listener.onResult(null);
        }
    }
}
