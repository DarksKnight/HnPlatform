package cn.ihuoniao.activity;

import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.squareup.otto.Subscribe;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Date;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.event.AppConfigEvent;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.firstdeploy.FirstDeployView;
import cn.ihuoniao.platform.splash.SplashView;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.BridgeWebView;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.platform.webview.DefaultHandler;
import cn.ihuoniao.store.AppConfigStore;

public class MainActivity extends BaseActivity {

    private BridgeWebView bwvContent = null;

    private Button btn = null;

    private LVCircularRing lvc = null;

    private RelativeLayout rl = null;

    private SplashView spv = null;

    private FirstDeployView firstDeployView = null;

    private String currentUrl = "";

    private int splashSecond = 3;

    private int dismissSecond = 4;

    private String url = "http://img5.duitang.com/uploads/item/201412/09/20141209002455_fShKH.jpeg";

    private boolean isLoading = true;

    private boolean isLoadFinish = false;

    private long oldTime = 0;

    private boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void initView() {
        super.initView();

        bwvContent = getView(R.id.bwv_content);
        btn = getView(R.id.btn);
        lvc = getView(R.id.lvc_loading);
        rl = getView(R.id.rl_content);
        lvc.setBarColor(getResources().getColor(R.color.colorTitle));

        //判断是否第一次启动
        SharedPreferences setting = getSharedPreferences(Constant.HN_SETTING, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (!user_first) {
            spv = new SplashView(this);
            rl.addView(spv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            setting.edit().putBoolean("FIRST", false).commit();
            firstDeployView = new FirstDeployView(this);
            rl.addView(firstDeployView,
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            firstDeployView.setListener(new FirstDeployView.Listener() {
                @Override
                public void closeView() {
                    if (isLoadFinish) {
                        firstDeployView.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, "正在初始化", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bwvContent.reload();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        oldTime = (new Date()).getTime();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoadFinish && !isLoading) {
                    dismissSplash();
                    showLoading();
                }
            }
        }, splashSecond * 1000);

        if (null != spv) {
            spv.setUrl(url);
        }

        registerStore(new AppConfigStore());
        actionsCreator.request_getAppConfig();

        bwvContent.setDefaultHandler(new DefaultHandler());
        bwvContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        bwvContent.getSettings().setJavaScriptEnabled(true);
        bwvContent.getSettings().setUseWideViewPort(true);
        bwvContent.getSettings().setLoadWithOverviewMode(true);
        bwvContent.getSettings().setDomStorageEnabled(true);

        bwvContent.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                lvc.setVisibility(View.VISIBLE);
                lvc.startAnim();
                currentUrl = url;
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        bwvContent.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType,
                    String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 60) {
                    isLoading = false;
                    lvc.stopAnim();
                    lvc.setVisibility(View.GONE);
                    dismissSplash();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        bwvContent.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("data : " + data);
                function.onCallBack("hello world");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bwvContent.canGoBack()) {
            lvc.setVisibility(View.VISIBLE);
            lvc.startAnim();
            bwvContent.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onStoreChange(AppConfigEvent event) {
        if (null != firstDeployView) {
            firstDeployView.setUrls();
        }

        isLoadFinish = true;
        Logger.i("platform url : " + Constant.APP_INFO.platformUrl);
        currentUrl = Constant.APP_INFO.platformUrl;
        bwvContent.loadUrl(Constant.APP_INFO.platformUrl);
        if ((new Date()).getTime() - oldTime > (1000 * dismissSecond) && !isLoading) {
            dismissSplash();
            showLoading();
        }
    }

    private void dismissSplash() {
        if (null != spv) {
            spv.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        if (isLoading) {
            lvc.setVisibility(View.VISIBLE);
            lvc.startAnim();
        }
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
