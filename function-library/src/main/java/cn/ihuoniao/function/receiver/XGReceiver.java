package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

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

        PushServiceFactory.init(activity.getApplication());
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(activity, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Logger.i("aliyun push success : " + response);
                listener.onResult(true);
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Logger.i("aliyun push fail code : " + errorCode + " errorMessage : " + errorMessage);
                listener.onResult(true);
            }
        });
        pushService.turnOnPushChannel(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                listener.onResult(true);
            }

            @Override
            public void onFailed(String s, String s1) {
                listener.onResult(true);
            }
        });

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
                listener.onResult(false);
            }
        });

//        final PushAgent mPushAgent = PushAgent.getInstance(activity);
//        if (!mPushAgent.isPushCheck()) {
//            new Handler(activity.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    mPushAgent.register(new IUmengRegisterCallback() {
//                        @Override
//                        public void onSuccess(String s) {
//                            Logger.i("umeng push success : " + s);
//                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
//                            listener.onResult(true);
//                        }
//
//                        @Override
//                        public void onFailure(String s, String s1) {
//                            Logger.i("umeng push error : " + s + " : " + s1);
//                            SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
//                            listener.onResult(true);
//                        }
//                    });
//                }
//            });
//        }

        if (passport.trim().length() != 0) {
            SPUtils.pushString("pushPassport", passport);
//            mPushAgent.addAlias(passport, "userID", new UTrack.ICallBack() {
//                @Override
//                public void onMessage(boolean isSuccess, String message) {
//                    Logger.i("umeng msg : " + message);
//                }
//            });
            pushService.bindAccount(passport, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Logger.i("ali push bindAccount : " + s);
                }

                @Override
                public void onFailed(String s, String s1) {

                }
            });
            pushService.addAlias(passport, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Logger.i("ali push alias : " + s);
                }

                @Override
                public void onFailed(String s, String s1) {

                }
            });
        }

//        mPushAgent.enable(new IUmengCallback() {
//            @Override
//            public void onSuccess() {
//                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
//                listener.onResult(true);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                SPUtils.pushBoolean("pushStatus", true);
//                listener.onResult(true);
//            }
//        });
    }

    public void unregister(Activity activity, ResultListener listener) {
        XGPushManager.unregisterPush(activity);
        SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);

//        PushAgent mPushAgent = PushAgent.getInstance(activity);
//        mPushAgent.disable(new IUmengCallback() {
//            @Override
//            public void onSuccess() {
//                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//
//            }
//        });
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.turnOffPushChannel(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
            }

            @Override
            public void onFailed(String s, String s1) {

            }
        });

        listener.onResult(null);
    }

}
