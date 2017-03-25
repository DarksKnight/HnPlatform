package cn.ihuoniao.function.receiver;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatLoginReceiver extends Receiver {

    public void login(IWXAPI wxapi) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        wxapi.sendReq(req);
    }
}
