package cn.ihuoniao.platform.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.ihuoniao.base.BaseActivity;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appInfo.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        infos.put("response", baseResp);
        actionsCreator.request_getWeChatLoginInfo();
        finish();
    }

    @Override
    public void registerStores() {

    }
}
