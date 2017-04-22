package cn.ihuoniao;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.model.AppInfoModel;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class HnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logger.i("xgpush success : " + o.toString());
                AppInfoModel.INSTANCE.pushStatus = "on";
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Logger.i("xgpush failed : " + s + " code : " + i);
                AppInfoModel.INSTANCE.pushStatus = "off";
            }
        });
        CrashReport.initCrashReport(getApplicationContext(), "2d9143b360", false);
    }
}
