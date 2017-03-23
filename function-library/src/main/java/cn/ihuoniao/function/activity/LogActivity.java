package cn.ihuoniao.function.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import cn.ihuoniao.function.R;
import cn.ihuoniao.function.util.FileUtils;
import cn.ihuoniao.function.util.LogCatUtil;

/**
 * Created by apple on 2017/3/19.
 */

public class LogActivity extends Activity implements View.OnClickListener {

    private TextView tvLog = null;
    private Button btnSave = null;
    private String log = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        tvLog = (TextView) findViewById(R.id.tv_log);
        btnSave = (Button) findViewById(R.id.btnSave);

        log = LogCatUtil.getLog();
        tvLog.setText(log);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/hn_platform/log/log_" + (new Date()).getTime() + ".txt";
            try {
                FileUtils.writeFileFromString(path, log, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
