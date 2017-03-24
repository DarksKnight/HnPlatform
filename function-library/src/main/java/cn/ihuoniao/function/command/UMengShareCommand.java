package cn.ihuoniao.function.command;

import android.app.Activity;

import com.umeng.socialize.UMShareListener;

import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.UMengShareReceiver;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class UMengShareCommand extends Command<Object, UMengShareReceiver> {

    public UMengShareCommand(UMengShareReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Activity activity = (Activity)params.get("activity");
        String title = params.get("umengShareTitle").toString();
        String summary = params.get("umengShareSummary").toString();
        String url = params.get("umengShareUrl").toString();
        String imageUrl = params.get("umengShareImageUrl").toString();
        UMShareListener umShareListener = (UMShareListener)params.get("umengShareListener");
        receiver.shareUmeng(activity, title, summary, url, imageUrl, umShareListener);
    }
}
