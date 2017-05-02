package cn.ihuoniao.platform.headview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.andview.refreshview.callback.IHeaderCallBack;

import cn.ihuoniao.platform.R;

/**
 * Created by sdk-app-shy on 2017/5/2.
 */

public class CustomHeadView extends LinearLayout implements IHeaderCallBack {

    public CustomHeadView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.view_header, null);
    }

    @Override
    public void onStateNormal() {

    }

    @Override
    public void onStateReady() {

    }

    @Override
    public void onStateRefreshing() {

    }

    @Override
    public void onStateFinish(boolean b) {

    }

    @Override
    public void onHeaderMove(double v, int i, int i1) {

    }

    @Override
    public void setRefreshTime(long l) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public int getHeaderHeight() {
        return 0;
    }
}
