package cn.ihuoniao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.listener.OnBottomLoadMoreTime;
import com.andview.refreshview.listener.OnTopRefreshTime;
import com.baidu.mapapi.BMapManager;
import com.jindianshenghuo.platform.R;
import com.squareup.otto.Subscribe;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.UMShareAPI;

import cn.ihuoniao.Constant;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.event.AppEvent;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.listener.StatusListener;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.firstdeploy.FirstDeployView;
import cn.ihuoniao.platform.headview.CustomHeadView;
import cn.ihuoniao.platform.splash.SplashView;
import cn.ihuoniao.platform.webview.BridgeWebView;
import cn.ihuoniao.platform.webview.BridgeWebViewClient;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.platform.webview.DefaultHandler;
import cn.ihuoniao.store.AlipayStore;
import cn.ihuoniao.store.AppStore;
import cn.ihuoniao.store.QQStore;
import cn.ihuoniao.store.UMengStore;
import cn.ihuoniao.store.WeChatStore;
import cn.ihuoniao.store.WeiboStore;

public class MainActivity extends BaseActivity {

    public static BridgeWebView bwvContent = null;

    private RelativeLayout rlContent = null;

    private SplashView spv = null;

    private FirstDeployView firstDeployView = null;

    private boolean isClickAdv = false;

    public static boolean isLoadMainWeb = true;

    private ValueCallback<Uri> mUploadMessage = null;

    public ValueCallback<Uri[]> mUploadMessageForAndroid5 = null;

    private XRefreshView rl = null;

    private CallBackFunction function = null;

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
        rl = getView(R.id.refreshLayout);

        bwvContent.setLinearLayout(rl);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.hn_50dp), (int) getResources().getDimension(R.dimen.hn_50dp));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlContent.addView(lvc, lp);

        if (!getPackageName().contains("jindianshenghuo")) {
            //判断是否第一次启动
            if (!appInfo.isFirstRun) {
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
        } else {
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spv.setVisibility(View.GONE);
                    if (!appInfo.isLoadFinish) {
                        showLoading();
                    }
                }
            }, 2000);
        }

        rl.setCustomHeaderView(new CustomHeadView(this));
        rl.setPullLoadEnable(false);
        rl.setMoveFootWhenDisablePullLoadMore(false);
        rl.setOnBottomLoadMoreTime(new OnBottomLoadMoreTime() {
            @Override
            public boolean isBottom() {
                return false;
            }
        });
        disableRefresh();
        rl.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {
                    actionsCreator.do_hideNavigationBar();
                    bwvContent.loadUrl(bwvContent.getUrl());
                } else {
                    actionsCreator.do_showNavigationBar();
                }
            }
        });

        bwvContent.setDefaultHandler(new DefaultHandler());
        bwvContent.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
        bwvContent.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        bwvContent.getSettings().setUseWideViewPort(true);
        bwvContent.getSettings().setDisplayZoomControls(true);
        bwvContent.getSettings().setDomStorageEnabled(true);
        bwvContent.getSettings().setAllowFileAccess(true);
        bwvContent.getSettings().setSavePassword(false);

        bwvContent.setWebViewClient(new BridgeWebViewClient(bwvContent) {

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                CommonUtil.showAlertDialog(MainActivity.this, getString(R.string.alert_title), getString(R.string.network_error));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.i("url : " + url);
                try {
                    if (url.contains("http://") || url.contains("https://")) {
                        disableRefresh();
                        showLoading();
                    } else if (url.contains("tel:")) {
                        String phone = url.split(":")[1];
                        Uri uri = Uri.parse("tel:" + phone);
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        startActivity(intent);
                        return true;
                    } else if (url.contains("sms:")) {
                        String phone = url.split(":")[1];
                        Uri uri = Uri.parse("smsto:" + phone);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        sendIntent.putExtra("sms_body", "");
                        startActivity(sendIntent);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        bwvContent.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        bwvContent.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    this.openFileChooser(uploadMsg);
                }
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    this.openFileChooser(uploadMsg);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    mUploadMessageForAndroid5 = filePathCallback;
                    CommonUtil.openFunction(MainActivity.this, Constant.CODE_PICK_PIC_5);
                }
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                CommonUtil.openFunction(MainActivity.this, Constant.CODE_PICK_PIC_5);
            }

            @Override
            public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult result) {
                if (!s2.contains("网络连接错误")) {
                    CommonUtil.showAlertDialog(MainActivity.this, 2, s2, result);
                }
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, final JsResult result) {
                if (!s1.contains("网络连接错误")) {
                    CommonUtil.showAlertDialog(MainActivity.this, 1, s1, result);
                }
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (!message.contains("网络连接错误")) {
                    CommonUtil.showAlertDialog(MainActivity.this, 0, message, result);
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
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
                        rl.stopRefresh();
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

        if (getPackageName().contains("jindianshenghuo")) {
            bwvContent.loadUrl(Constant.HTML_JINGDIANSHENGHUO);
//            bwvContent.loadUrl("file:///android_asset/debuga.html");
            initWebView();
        } else {
            actionsCreator.request_getAppConfig();
        }

//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        UmengNotificationClickHandler messageHandler = new UmengNotificationClickHandler() {
//
//            @Override
//            public void launchApp(Context context, UMessage uMessage) {
//                super.launchApp(context, uMessage);
//                Map<String, String> info = uMessage.extra;
//                String url = info.get("url");
//                Logger.i("content url : " + url);
//                isLoadMainWeb = false;
//                bwvContent.loadUrl(url);
//            }
//        };
//        mPushAgent.setNotificationClickHandler(messageHandler);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bwvContent.canGoBack()) {
            if (getPackageName().contains("jindianshenghuo")) {
                if (!bwvContent.getUrl().contains("http://www.jindianshenghuo.com/?service=waimai&do=courier&template=index")) {
                    showLoading();
                    bwvContent.goBack();
                } else {
                    CommonUtil.exit(this);
                }
            } else {
                if (!bwvContent.getUrl().equals(appInfo.platformUrl + "/")) {
                    showLoading();
                    bwvContent.goBack();
                } else {
                    CommonUtil.exit(this);
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getPackageName().contains("jindianshenghuo")) {
                if (!bwvContent.getUrl().contains("http://www.jindianshenghuo.com/?service=waimai&do=courier&template=index")) {
                    showLoading();
                    bwvContent.loadUrl("http://www.jindianshenghuo.com/?service=waimai&do=courier&template=index");
                } else {
                    CommonUtil.exit(this);
                }
            } else {
                if (!bwvContent.getUrl().equals(appInfo.platformUrl + "/")) {
                    showLoading();
                    bwvContent.loadUrl(appInfo.platformUrl);
                } else {
                    CommonUtil.exit(this);
                }
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

        appInfo.platformUrl = event.appConfig.cfg_android_index;
        appInfo.loginInfo = event.appConfig.cfg_loginconnect;
        if (!isClickAdv) {
            if (isLoadMainWeb) {
                bwvContent.loadUrl(appInfo.platformUrl);
//                bwvContent.loadUrl("file:///android_asset/debuga.html");
            } else {
                isLoadMainWeb = true;
            }
        }

        initWebView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CODE_PICK_PIC) {
            if (null != mUploadMessage) {
                if (null != data) {
                    Uri result = data.getData();
                    mUploadMessage.onReceiveValue(result);
                } else {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = null;
            }
        } else if (requestCode == Constant.CODE_PICK_PIC_5) {
            if (null != mUploadMessageForAndroid5) {
                if (null != data) {
                    Uri result = data.getData();
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(null);
                }
                mUploadMessageForAndroid5 = null;
            }
        } else if (requestCode == Constant.CODE_SCAN_RESULT) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = data.getStringExtra("result");
                if (result.contains("http")) {
                    bwvContent.loadUrl(result);
                } else {
                    CommonUtil.toast(this, result);
                }
                if (null != function) {
                    Logger.i("scan result : " + result);
                    function.onCallBack(result);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWebView() {
        infos.put("qqAppId", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.qq).getString("appid") : "");
        infos.put("qqAppKey", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.qq).getString("appkey") : "");
        infos.put("wechatAppId", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.wechat).getString("appid") : "");
        infos.put("wechatSecret", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.wechat).getString("appsecret") : "");
        infos.put("weiboAkey", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.sina).getString("akey") : "");
        infos.put("weiboSkey", null != appInfo.loginInfo ? JSON.parseObject(appInfo.loginInfo.sina).getString("skey") : "");
        actionsCreator.init_umeng();
        actionsCreator.init_location();

        actionsCreator.register_getAppInfo();
        actionsCreator.register_updateBadgeValue();
        actionsCreator.register_getPushStatus();
        actionsCreator.register_setPushStatus();
        actionsCreator.register_getCacheSize();
        actionsCreator.register_clearCache();
        actionsCreator.register_appLogout();
        actionsCreator.register_appLoginFinish();
        actionsCreator.register_umengShare();
        actionsCreator.register_qqLogin();
        actionsCreator.register_wechatLogin();
        actionsCreator.register_weiboLogin();
        actionsCreator.register_alipay();
        actionsCreator.register_wechatPay();
        actionsCreator.register_qr_scan(new ResultListener<CallBackFunction>() {
            @Override
            public void onResult(CallBackFunction result) {
                function = result;
            }
        });
        actionsCreator.register_setDragRefresh(new ResultListener<String>() {
            @Override
            public void onResult(String result) {
                if (result.equals("on")) {
                    enbleRefresh();
                } else {
                    disableRefresh();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (null != click) {
            String content = click.getCustomContent();
            String url = JSON.parseObject(content).getString("url");
            Logger.i("content url : " + url);
            isLoadMainWeb = false;
            bwvContent.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BMapManager.destroy();
    }

    private void enbleRefresh() {
        rl.setPullRefreshEnable(true);
        rl.setMoveHeadWhenDisablePullRefresh(true);
        rl.setOnTopRefreshTime(new OnTopRefreshTime() {
            @Override
            public boolean isTop() {
                return bwvContent.getWebScrollY() == 0;
            }
        });
    }

    private void disableRefresh() {
        rl.setPullRefreshEnable(false);
        rl.setMoveHeadWhenDisablePullRefresh(false);
        rl.setOnTopRefreshTime(new OnTopRefreshTime() {
            @Override
            public boolean isTop() {
                return false;
            }
        });
    }
}
