package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

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

        final PushAgent mPushAgent = PushAgent.getInstance(activity);
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                mPushAgent.enable(new IUmengCallback() {
                    @Override
                    public void onSuccess() {
                        if (passport.trim().length() != 0) {
                            mPushAgent.addExclusiveAlias(passport, "userID", new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean isSuccess, String message) {
                                    Logger.i("umeng msg : " + message);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

    }

    public void unregister(Activity activity, ResultListener listener) {
        XGPushManager.unregisterPush(activity);

        PushAgent mPushAgent = PushAgent.getInstance(activity);
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        listener.onResult(null);
    }

}
