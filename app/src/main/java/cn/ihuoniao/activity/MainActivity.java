package cn.ihuoniao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ldoublem.loadingviewlib.view.LVCircularRing;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.BridgeWebView;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.platform.webview.DefaultHandler;

public class MainActivity extends BaseActivity {

    private BridgeWebView bwvContent = null;
    private Button btn = null;
    private LVCircularRing lvc = null;
    private RelativeLayout rl = null;

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
        rl = getView(R.id.rl);
        lvc.setBarColor(getResources().getColor(R.color.colorTitle));
        lvc.startAnim();

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
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        bwvContent.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
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
                if (newProgress > 80) {
                    lvc.stopAnim();
                    lvc.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        Logger.i("platform url : " + Constant.APP_INFO.platformUrl);
        bwvContent.loadUrl(Constant.APP_INFO.platformUrl);

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
            bwvContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
