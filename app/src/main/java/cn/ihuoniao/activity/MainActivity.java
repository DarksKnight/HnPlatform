package cn.ihuoniao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.UMShareAPI;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.event.AppEvent;
import cn.ihuoniao.function.listener.StatusListener;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.firstdeploy.FirstDeployView;
import cn.ihuoniao.platform.splash.SplashView;
import cn.ihuoniao.platform.webview.BridgeWebView;
import cn.ihuoniao.platform.webview.BridgeWebViewClient;
import cn.ihuoniao.platform.webview.DefaultHandler;
import cn.ihuoniao.store.AlipayStore;
import cn.ihuoniao.store.AppStore;
import cn.ihuoniao.store.QQStore;
import cn.ihuoniao.store.UMengStore;
import cn.ihuoniao.store.WeChatStore;
import cn.ihuoniao.store.WeiboStore;

public class MainActivity extends BaseActivity {

    private BridgeWebView bwvContent = null;

    private RelativeLayout rlContent = null;

    private SplashView spv = null;

    private FirstDeployView firstDeployView = null;

    private boolean isClickAdv = false;

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
        rlContent = getView(R.id.rl_content);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.hn_50dp), (int) getResources().getDimension(R.dimen.hn_50dp));
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
                    isClickAdv = true;
                    showLoading();
                    bwvContent.loadUrl(url);
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

        bwvContent.setDefaultHandler(new DefaultHandler());
        bwvContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        bwvContent.getSettings().setJavaScriptEnabled(true);
        bwvContent.getSettings().setUseWideViewPort(true);
        bwvContent.getSettings().setLoadWithOverviewMode(true);
        bwvContent.getSettings().setDomStorageEnabled(true);
        bwvContent.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        bwvContent.setWebViewClient(new BridgeWebViewClient(bwvContent) {

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                Logger.i("url : " + url);
                if (url.contains("http://")) {
                    showLoading();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        bwvContent.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {

            @Override
            public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult result) {
                showAlertDialog(2, s2, result);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, final JsResult result) {
                showAlertDialog(1, s1, result);
                return true;
            }

            @Override
            public boolean onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                showAlertDialog(0, message, result);
                return true;
            }

            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                if (isClickAdv) {
                    if (null != view.getOriginalUrl()) {
                        if (view.getOriginalUrl().equals(spv.getLink())) {
                            if (newProgress > 70) {
                                isClickAdv = false;
                                hideLoading();
                            }
                        }
                    }
                } else {
                    if (newProgress > 70) {
                        appInfo.isLoadFinish = true;
                        hideLoading();
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        infos.put("webview", bwvContent);
        infos.put("activity", this);
        infos.put("statusListener", new StatusListener() {

            @Override
            public void start() {
                showLoading();
            }

            @Override
            public void end() {
                hideLoading();
            }
        });
    }

    @Override
    public void registerStores() {
        registerStore(TYPE.REGISTER_STORE_APP, new AppStore());
        registerStore(TYPE.REGISTER_STORE_QQ, new QQStore());
        registerStore(TYPE.REGISTER_STORE_WECHAT, new WeChatStore());
        registerStore(TYPE.REGISTER_STROE_WEIBO, new WeiboStore());
        registerStore(TYPE.REGISTER_STORE_UMENG, new UMengStore());
        registerStore(TYPE.REGISTER_STORE_ALIPAY, new AlipayStore());
    }

    @Override
    protected void initData() {
        super.initData();
        actionsCreator.request_getAppConfig();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bwvContent.canGoBack()) {
            if (!bwvContent.getUrl().equals(appInfo.platformUrl + "/")) {
                showLoading();
                bwvContent.goBack();
            } else {
                CommonUtil.exit(this);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!bwvContent.getUrl().equals(appInfo.platformUrl + "/")) {
                showLoading();
                bwvContent.loadUrl(appInfo.platformUrl);
            } else {
                CommonUtil.exit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onStoreChange(final AppEvent event) {
        if (null != firstDeployView) {
            firstDeployView.setUrls(event.appConfig.cfg_guide.android);
        }
        if (null != spv) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CommonUtil.isShowAdv(MainActivity.this, Constant.HN_SETTING, event.appConfig.cfg_startad.link)) {
                        spv.setUrl(event.appConfig.cfg_startad.src, event.appConfig.cfg_startad.link, event.appConfig.cfg_startad.time);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spv.setVisibility(View.GONE);
                                if (!appInfo.isLoadFinish) {
                                    showLoading();
                                }
                            }
                        }, Integer.parseInt(event.appConfig.cfg_startad.time) * 1000);
                    }
                }
            }, 1000);
        }

        appInfo.platformUrl = event.appConfig.cfg_basehost;
        appInfo.loginInfo = event.appConfig.cfg_loginconnect;
        if (!isClickAdv) {
            bwvContent.loadUrl(appInfo.platformUrl);
        }

        initWebView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWebView() {
        infos.put("qqAppId", JSON.parseObject(appInfo.loginInfo.qq).getString("appid"));
        infos.put("qqAppKey", JSON.parseObject(appInfo.loginInfo.qq).getString("appkey"));
        infos.put("wechatAppId", JSON.parseObject(appInfo.loginInfo.wechat).getString("appid"));
        infos.put("wechatSecret", JSON.parseObject(appInfo.loginInfo.wechat).getString("appsecret"));
        infos.put("weiboAkey", JSON.parseObject(appInfo.loginInfo.sina).getString("akey"));
        infos.put("weiboSkey", JSON.parseObject(appInfo.loginInfo.sina).getString("skey"));
        actionsCreator.init_umeng();

        actionsCreator.register_getAppInfo();
        actionsCreator.register_appLogout();
        actionsCreator.register_appLoginFinish();
        actionsCreator.register_umengShare();
        actionsCreator.register_qqLogin();
        actionsCreator.register_wechatLogin();
        actionsCreator.register_weiboLogin();
        actionsCreator.register_alipay();
        actionsCreator.register_wechatPay();
    }

    private void showAlertDialog(int type, String message, final JsResult result) {
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setMessage(message);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        });
        if (type != 0) {
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    result.cancel();
                }
            });
        }
        b.setCancelable(true);
        b.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        b.create().show();
    }
}
