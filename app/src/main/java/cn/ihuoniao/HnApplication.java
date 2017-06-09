package cn.ihuoniao;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;

import cn.ihuoniao.function.constant.FunctionConstants;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.function.util.SPUtils;
import cn.ihuoniao.model.AppInfoModel;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class HnApplication extends Application {

    private AppInfoModel appInfoModel = AppInfoModel.INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        Config.DEBUG = true;
        SDKInitializer.initialize(this);
        BMapManager.init();
        SPUtils.setApplication(this);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Logger.i("onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };

        XGPushConfig.enableDebug(this, true);
        QbSdk.initX5Environment(getApplicationContext(), cb);
        Fresco.initialize(this);

        if (!getPackageName().contains("jindianshenghuo")) {
            if (CommonUtil.isFirstRun(this, Constant.HN_SETTING)) {
                appInfoModel.isFirstRun = true;
                registerPush();
            } else {
                appInfoModel.isFirstRun = false;
                if (SPUtils.getBoolean(FunctionConstants.PUSH_STATUS)) {
                    appInfoModel.pushStatus = "on";
                    registerPush();
                } else {
                    appInfoModel.pushStatus = "off";
                }
            }
        } else {
            registerPush();
        }

        CrashReport.initCrashReport(getApplicationContext(), "2d9143b360", false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void registerPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Logger.i("umeng push success : " + s);
                appInfoModel.pushToken = s;
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.i("umeng push error : " + s + " : " + s1);
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
            }
        });
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logger.i("xgpush success : " + o.toString());
                appInfoModel.pushStatus = "on";
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, true);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Logger.i("xgpush failed : " + s + " code : " + i);
                appInfoModel.pushStatus = "off";
                SPUtils.pushBoolean(FunctionConstants.PUSH_STATUS, false);
            }
        });
    }
}
