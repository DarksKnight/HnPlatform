package cn.ihuoniao.platform.wxapi;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import cn.ihuoniao.function.util.Logger;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.i("req : " + resp.errStr + " : " + resp.errCode);
        super.onResp(resp);
    }
}
