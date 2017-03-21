package cn.ihuoniao.platform.splash;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Timer;
import java.util.TimerTask;

import cn.ihuoniao.platform.R;

/**
 * Created by apple on 2017/3/20.
 */

public class SplashView extends LinearLayout {

    private SimpleDraweeView sdv = null;
    private String link = "";
    private int advTime = 0;
    private Timer timer = new Timer();
    private TextView tvAdvTime = null;
    private Listener listener = null;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    advTime--;
                    tvAdvTime.setText(advTime + "");
                    if (advTime == -1) {
                        setVisibility(GONE);
                        listener.onComplete();
                        return;
                    }
                }
            });
        }
    };


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
        sdv.setEnabled(false);
        tvAdvTime = (TextView) findViewById(R.id.tv_advTime);

        sdv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE);
                listener.onClickAdv(link);
            }
        });
    }

    public void setUrl(String url, String link, String time) {
        Uri uri = Uri.parse(url);
        sdv.setImageURI(uri);
        sdv.setEnabled(true);
        this.link = link;
        tvAdvTime.setVisibility(View.VISIBLE);
        tvAdvTime.setText(time);
        advTime = Integer.parseInt(time);
        if (null != timer) {
            timer.schedule(task, 1000, 1000);
        }
    }

    public String getLink() {
        return link;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisibility(int visibility) {
        timer.cancel();
        timer = null;
        super.setVisibility(visibility);
    }

    public interface Listener {
        void onComplete();
        void onClickAdv(String url);
    }
}
