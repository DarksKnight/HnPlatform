package cn.ihuoniao.function.receiver;

import android.content.Context;

import com.tencent.tauth.Tencent;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQInitReceiver extends Receiver {

    public Tencent init(Context context, String appId) {
        return Tencent.createInstance(appId, context);
    }
}
