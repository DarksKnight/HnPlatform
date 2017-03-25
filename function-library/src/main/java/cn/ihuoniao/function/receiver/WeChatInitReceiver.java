package cn.ihuoniao.function.receiver;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatInitReceiver extends Receiver {

    public IWXAPI init(Context context, String appId) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, appId);
        iwxapi.registerApp(appId);
        return iwxapi;
    }
}
