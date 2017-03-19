package cn.ihuoniao.function.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ihuoniao.function.R;
import cn.ihuoniao.function.util.LogCatUtil;

/**
 * Created by apple on 2017/3/19.
 */

public class LogActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLog = null;
    private Button btnSave = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        tvLog = (TextView) findViewById(R.id.tv_log);
        btnSave = (Button) findViewById(R.id.btnSave);

        tvLog.setText(LogCatUtil.getLog());
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {

        }
    }
}
