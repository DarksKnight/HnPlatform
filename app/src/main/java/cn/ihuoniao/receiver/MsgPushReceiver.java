package cn.ihuoniao.receiver;

import android.content.Context;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import cn.ihuoniao.function.util.Logger;

/**
 * Created by sdk-app-shy on 2017/4/12.
 */

public class MsgPushReceiver extends XGPushBaseReceiver {

    private Listener listener = null;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Logger.i("aaaaaa");
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        Logger.i("kkkkk");
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        String customContent = xgPushClickedResult.getCustomContent();
        Logger.i("customContent : " + customContent);
        if (null != listener) {
            listener.receiver(customContent);
        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
    }

    public interface Listener {
        void receiver(String content);
    }
}
