package cn.ihuoniao.function.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.ihuoniao.function.R;
import cn.ihuoniao.function.util.LogCatUtil;

/**
 * Created by apple on 2017/3/19.
 */

public class LogActivity extends AppCompatActivity {

    private TextView tvLog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        tvLog = (TextView) findViewById(R.id.tv_log);

        tvLog.setText(LogCatUtil.getLog());
    }
}
