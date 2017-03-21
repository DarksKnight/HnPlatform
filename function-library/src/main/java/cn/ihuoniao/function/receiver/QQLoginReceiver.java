package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQLoginReceiver extends Receiver {

    public void login(Tencent tencent, Activity activity, IUiListener listener) {
        tencent.login(activity, "all", listener);
    }
}
