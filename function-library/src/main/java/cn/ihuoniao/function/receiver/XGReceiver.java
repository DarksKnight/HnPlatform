package cn.ihuoniao.function.receiver;

import android.app.Activity;
import android.os.Handler;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.constant.FunctionConstants;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.function.util.SPUtils;

/**
 * Created by apple on 2017/3/26.
 */

public class XGReceiver extends Receiver {

    public void register(final Activity activity, final String passport, final ResultListener listener) {
        Logger.i("push passport : " + passport);
        XGPushManager.unregisterPush(activity, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logger.i("xgpush unregister success");
                if (passport.trim().length() == 0) {
                    XGPushManager.registerPush(activity, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
                            Logger.i("xgpush register success, no passport");
                            listener.onResult(true);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Logger.i("xgpush register fail : " + s);
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
                            listener.onResult(false);
                        }
                    });
                } else {
                    XGPushManager.registerPush(activity, passport, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            Logger.i("xgpush register success");
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
                            listener.onResult(true);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Logger.i("xgpush register fail : " + s);
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
                            listener.onResult(false);
                        }
                    });
                }
            }

            @Override
            public void onFail(Object o, int i, String s) {
                SPUtils.pushBoolean("pushStatus", false);
            }
        });

        final PushAgent mPushAgent = PushAgent.getInstance(activity);
        if (!mPushAgent.isPushCheck()) {
            new Handler(activity.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mPushAgent.register(new IUmengRegisterCallback() {
                        @Override
                        public void onSuccess(String s) {
                            Logger.i("umeng push success : " + s);
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                            Logger.i("umeng push error : " + s + " : " + s1);
                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
                        }
                    });
                }
            });
        }

        if (passport.trim().length() != 0) {
            mPushAgent.addAlias(passport, "userID", new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String message) {
                    Logger.i("umeng msg : " + message);
                }
            });
        }

        mPushAgent.enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
            }

            @Override
            public void onFailure(String s, String s1) {
                SPUtils.pushBoolean("pushStatus", true);
            }
        });
    }

    public void unregister(Activity activity, ResultListener listener) {
        XGPushManager.unregisterPush(activity);
        SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);

        PushAgent mPushAgent = PushAgent.getInstance(activity);
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        listener.onResult(null);
    }

}
