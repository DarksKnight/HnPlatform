package cn.ihuoniao.function.command;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.QQShareReceiver;
import cn.ihuoniao.function.util.CommonUtil;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public class QQShareCommand extends Command<Object, QQShareReceiver> {

    public QQShareCommand(QQShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        Tencent tencent = (Tencent)params.get("tencent");
        IUiListener iUiListener = (IUiListener)params.get("listener");
        String title = params.get("qqTitle").toString();
        String summary = params.get("qqSummary").toString();
        String url = params.get("qqUrl").toString();
        String imageUrl = params.get("qqImageUrl").toString();
        receiver.shareQQ(tencent, activity, title, summary, url, imageUrl, CommonUtil.getAppName(activity), iUiListener);
    }
}
