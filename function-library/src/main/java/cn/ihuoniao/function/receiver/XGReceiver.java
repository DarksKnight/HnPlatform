package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.util.Logger;

/**
 * Created by apple on 2017/3/26.
 */

public class XGReceiver extends Receiver {

    public void register(final Activity activity, final String passport, final ResultListener listener) {
        XGPushManager.unregisterPush(activity, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logger.i("xgpush unregister success");
                if (passport.trim().length() == 0) {
                    XGPushManager.registerPush(activity, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            Logger.i("xgpush register success, no passport");
                            listener.onResult(true);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Logger.i("xgpush register fail : " + s);
                            listener.onResult(false);
                        }
                    });
                } else {
                    XGPushManager.registerPush(activity, passport, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            Logger.i("xgpush register success");
                            listener.onResult(true);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Logger.i("xgpush register fail : " + s);
                            listener.onResult(false);
                        }
                    });
                }
            }

            @Override
            public void onFail(Object o, int i, String s) {

            }
        });
    }

    public void unregister(Activity activity, ResultListener listener) {
        XGPushManager.unregisterPush(activity);
        listener.onResult(null);
    }

}
