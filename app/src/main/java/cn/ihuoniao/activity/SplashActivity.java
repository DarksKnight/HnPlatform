package cn.ihuoniao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.store.AppConfigStore;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class SplashActivity extends BaseActivity {

    private SimpleDraweeView sdv = null;

    private int splashSecond = 3000;

    private int time = 0;

    private String url = "http://img5.duitang.com/uploads/item/201412/09/20141209002455_fShKH.jpeg";

    private boolean isClose = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable count = new Runnable() {
        @Override
        public void run() {
            time++;
            if (isClose) {
                handler.removeCallbacks(count);
                return;
            }
            if (time > 4 && !isClose) {
                handler.removeCallbacks(count);
                startMainActivity();
                return;
            }
            handler.postDelayed(count, 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isSetStatusColor = false;
        init();
    }

    @Override
    protected void initView() {
        super.initView();

        sdv = getView(R.id.sdv_splash);
    }

    @Override
    protected void initData() {
        super.initData();

        registerStore(new AppConfigStore());

        Uri uri = Uri.parse(url);
        sdv.setImageURI(uri);

        handler.post(count);

        actionsCreator.request_getAppConfig();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Constant.APP_INFO.isNeedFinish) {
                    startMainActivity();
                }
            }
        }, splashSecond);
    }

    private void startMainActivity() {
        isClose = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
