package cn.ihuoniao.platform.splash;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import cn.ihuoniao.platform.R;

/**
 * Created by apple on 2017/3/20.
 */

public class SplashView extends LinearLayout {

    private SimpleDraweeView sdv = null;
    private boolean isShow = true;

    public SplashView(Context context) {
        this(context, null, 0);
    }

    public SplashView(Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_splash, this);
        initView();
    }

    private void initView() {
        sdv = (SimpleDraweeView) findViewById(R.id.sdv_splash);
    }

    public void setUrl(String url) {
        Uri uri = Uri.parse(url);
        sdv.setImageURI(uri);
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public void setVisibility(int visibility) {
        isShow = false;
        super.setVisibility(visibility);
    }
}
