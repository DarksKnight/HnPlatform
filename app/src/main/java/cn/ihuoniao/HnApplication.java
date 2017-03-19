package cn.ihuoniao;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class HnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        CrashReport.initCrashReport(getApplicationContext(), "2d9143b360", false);
    }
}
