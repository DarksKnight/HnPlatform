package cn.ihuoniao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718966741&di=f62455c41c820a2095c62008b4626708&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201305%2F04%2F181712hd2hv6atncvqntga.jpg";

    private String[] urls = new String[]{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718948173&di=85bc7c9f75a14127280fdeb17325f88d&imgtype=0&src=http%3A%2F%2Fwww.1tong.com%2Fuploads%2Fallimg%2F130806%2F1-130P61045170-L.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718966741&di=f62455c41c820a2095c62008b4626708&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201305%2F04%2F181712hd2hv6atncvqntga.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718990213&di=bc975b26c7ff6a6fce82e5ad328b98ee&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201412%2F09%2F20141209002509_u5hrh.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490313725&di=17c700b87b64afc233b3017dbbde42cd&imgtype=jpg&er=1&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F244%2F64P3023HQL9Z.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489719012472&di=d1f707107e509de492b73a74b8231d4a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2F574f9b1001e93901b32412d17bec54e737d19655.jpg"};

    private long oldTime = 0;

    private Tencent tencent = null;

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
                params.put("listener", new IUiListener() {
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
                });
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

        oldTime = (new Date()).getTime();

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
}
