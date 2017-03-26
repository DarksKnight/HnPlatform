package cn.ihuoniao.function.receiver;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMWXHandler;

import android.app.Activity;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by apple on 2017/3/26.
 */

public class WeChatPayReceiver extends Receiver {

    public void pay(Activity activity, String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        UMShareAPI api = UMShareAPI.get(activity.getApplicationContext());
        UMWXHandler handler = (UMWXHandler) api.getHandler(SHARE_MEDIA.WEIXIN);
        handler.getWXApi().sendReq(request);
    }

}
