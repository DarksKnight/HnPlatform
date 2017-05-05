package cn.ihuoniao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.andview.refreshview.listener.OnTopRefreshTime;
import com.squareup.otto.Subscribe;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.UMShareAPI;

import java.util.Map;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
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
import cn.ihuoniao.receiver.MsgPushReceiver;
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

    private boolean isLoadMainWeb = true;

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

        rl.setCustomHeaderView(new CustomHeadView(this));
        rl.setPullLoadEnable(false);
        rl.setOnTopRefreshTime(new OnTopRefreshTime() {
            @Override
            public boolean isTop() {
                return bwvContent.getWebScrollY() == 0;
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
//        bwvContent.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
//        bwvContent.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        bwvContent.getSettings().setUseWideViewPort(true);
        bwvContent.getSettings().setDisplayZoomControls(true);
        bwvContent.getSettings().setDomStorageEnabled(true);
        bwvContent.getSettings().setAllowFileAccess(true);

        bwvContent.setWebViewClient(new BridgeWebViewClient(bwvContent) {

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                CommonUtil.showAlertDialog(MainActivity.this, getString(R.string.alert_title), getString(R.string.network_error));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.i("url : " + url);
                disableRefresh();
                try {
                    if (url.contains("http://") || url.contains("https://")) {
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
                CommonUtil.showAlertDialog(MainActivity.this, 2, s2, result);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, final JsResult result) {
                CommonUtil.showAlertDialog(MainActivity.this, 1, s1, result);
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                CommonUtil.showAlertDialog(MainActivity.this, 0, message, result);
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
        actionsCreator.request_getAppConfig();
        registerReceiver();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengNotificationClickHandler messageHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
                Map<String, String> info = uMessage.extra;
                String url = info.get("url");
                Logger.i("content url : " + url);
                isLoadMainWeb = false;
                bwvContent.loadUrl(url);
            }
        };
        mPushAgent.setNotificationClickHandler(messageHandler);
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
            if (isLoadMainWeb) {
//                bwvContent.loadUrl("http://ihuoniao.cn/android");
                if (isDebug) {
                    bwvContent.loadUrl("file:///android_asset/debug.html");
                } else {
                    bwvContent.loadUrl(appInfo.platformUrl);
                }
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
                if (null != function) {
                    Logger.i("scan result : " + result);
                    function.onCallBack(result);
                }
            }
        }
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

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
        filter.addAction("com.tencent.android.tpush.action.FEEDBACK");
        MsgPushReceiver receiver = new MsgPushReceiver();
        receiver.setListener(new MsgPushReceiver.Listener() {
            @Override
            public void receiver(String content) {
                if (null != content) {
                    String url = JSON.parseObject(content).getString("url");
                    bwvContent.loadUrl(url);
                }
            }
        });
        registerReceiver(receiver, filter);
    }

    private void enbleRefresh() {
        rl.setPullRefreshEnable(true);
        rl.setMoveHeadWhenDisablePullRefresh(true);
    }

    private void disableRefresh() {
        rl.setPullRefreshEnable(false);
        rl.setMoveHeadWhenDisablePullRefresh(false);
    }
}
