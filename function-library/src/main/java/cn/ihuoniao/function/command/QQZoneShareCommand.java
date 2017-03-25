package cn.ihuoniao.function.command;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.QQShareReceiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQZoneShareCommand extends Command<Object, QQShareReceiver> {

    public QQZoneShareCommand(QQShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        Tencent tencent = (Tencent)params.get("tencent");
        IUiListener iUiListener = (IUiListener)params.get("listener");
        String title = params.get("qqZoneTitle").toString();
        String summary = params.get("qqZoneSummary").toString();
        String url = params.get("qqZoneUrl").toString();
        ArrayList<String> imageUrls = (ArrayList<String>)params.get("imageUrls");
        receiver.shareQQZone(tencent, activity, title, summary, url, imageUrls, iUiListener);
    }
}
