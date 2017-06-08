package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.listener.ResultListener;

/**
 * Created by sdk-app-shy on 2017/6/8.
 */

public class PushUnRegisterReceiver extends Receiver {

    public void unregister(Activity activity, String passport, ResultListener listener) {
        PushAgent mPushAgent = PushAgent.getInstance(activity);
        mPushAgent.removeAlias(passport, "userID", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {

            }
        });
        listener.onResult(null);
    }
}
