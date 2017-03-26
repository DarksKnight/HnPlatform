package cn.ihuoniao.function.receiver;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import android.app.Activity;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.util.Logger;

/**
 * Created by apple on 2017/3/26.
 */

public class XGReceiver extends Receiver {

    public void register(final Activity activity, final String passport) {
        XGPushManager.unregisterPush(activity, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logger.i("xgpush unregister success");
                XGPushManager.registerPush(activity, passport, new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        Logger.i("xgpush register success");
                    }

                    @Override
                    public void onFail(Object o, int i, String s) {
                        Logger.i("xgpush register fail : " + s);
                    }
                });
            }

            @Override
            public void onFail(Object o, int i, String s) {

            }
        });

    }

}
