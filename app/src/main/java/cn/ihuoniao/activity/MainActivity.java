package cn.ihuoniao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.otto.Subscribe;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.event.AppConfigEvent;
import cn.ihuoniao.function.command.QQInitCommand;
import cn.ihuoniao.function.command.QQLoginCommand;
import cn.ihuoniao.function.receiver.QQInitReceiver;
import cn.ihuoniao.function.receiver.QQLoginReceiver;
import cn.ihuoniao.function.util.CommonUtil;
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

    private RelativeLayout rlContent = null;

    private SplashView spv = null;

    private FirstDeployView firstDeployView = null;

    private Tencent tencent = null;

    private IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Logger.i("com : " + o.toString());
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
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
        rlContent = getView(R.id.rl_content);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.hn_50dp), (int)getResources().getDimension(R.dimen.hn_50dp));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlContent.addView(lvc, lp);

        //判断是否第一次启动
        if (!CommonUtil.isFirstRun(this, Constant.HN_SETTING)) {
            spv = new SplashView(this);
            rlContent.addView(spv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            spv.setListener(new SplashView.Listener() {
                @Override
                public void onComplete() {
                    if (!appInfo.isLoadFinish) {
                        showLoading();
                    }
                }

                @Override
                public void onClickAdv(String url) {
                    bwvContent.loadUrl(url);
                    dissmissSplash();
                    showLoading();
                }
            });
        } else {
            firstDeployView = new FirstDeployView(this);
            rlContent.addView(firstDeployView,
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            firstDeployView.setListener(new FirstDeployView.Listener() {
                @Override
                public void closeView() {
                    if (appInfo.isLoadFinish) {
                        firstDeployView.setVisibility(View.GONE);
                    } else {
                        CommonUtil.toast(MainActivity.this, getString(R.string.toast_init));
                    }
                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", MainActivity.this);
                params.put("listener", iUiListener);
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        registerStore(new AppConfigStore());
        actionsCreator.request_getAppConfig();
        Map<String, Object> params = new HashMap<>();
        params.put("context", this);
        params.put("appId", "1105976281");
        tencent = (Tencent)control.doCommand(new QQInitCommand(new QQInitReceiver()), params);

        bwvContent.setDefaultHandler(new DefaultHandler());
        bwvContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        bwvContent.getSettings().setJavaScriptEnabled(true);
        bwvContent.getSettings().setUseWideViewPort(true);
        bwvContent.getSettings().setLoadWithOverviewMode(true);
        bwvContent.getSettings().setDomStorageEnabled(true);

        bwvContent.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showLoading();
                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        bwvContent.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle(getString(R.string.alert_title));
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(true);
                b.create().show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 60) {
                    appInfo.isLoadFinish = true;
                    hideLoading();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        bwvContent.registerHandler("qqLogin", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Map<String, Object> params = new HashMap<>();
                params.put("tencent", tencent);
                params.put("activity", MainActivity.this);
                params.put("listener", iUiListener);
                control.doCommand(new QQLoginCommand(new QQLoginReceiver()), params);
                function.onCallBack("hello world");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bwvContent.canGoBack()) {
            showLoading();
            bwvContent.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommonUtil.exit(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onStoreChange(final AppConfigEvent event) {
        if (null != firstDeployView) {
            firstDeployView.setUrls(event.appConfig.cfg_guide.android);
        }
        if (null != spv) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spv.setUrl(event.appConfig.cfg_startad.src, event.appConfig.cfg_startad.link, event.appConfig.cfg_startad.time);
                }
            }, 1000);
        }

        appInfo.platformUrl = event.appConfig.cfg_basehost;
        Logger.i("platform url : " + appInfo.platformUrl);
        bwvContent.loadUrl(appInfo.platformUrl);
//        bwvContent.loadUrl("http://192.168.21.61:7001/login");
    }

    private void dissmissSplash() {
        if (null != spv) {
            spv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.handleResultData(data, iUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
