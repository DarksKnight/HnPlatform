package cn.ihuoniao.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cn.ihuoniao.R;
import cn.ihuoniao.base.BaseActivity;
import cn.ihuoniao.function.util.LogCatUtil;

/**
 * Created by sdk-app-shy on 2017/3/18.
 */

public class LogActivity extends BaseActivity {

    private TextView tv = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        init();
    }

    @Override
    protected void initView() {
        super.initView();

        tv = getView(R.id.tv_log);
        tv.setText(LogCatUtil.getLog());
    }
}
