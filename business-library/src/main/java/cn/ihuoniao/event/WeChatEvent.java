package cn.ihuoniao.event;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import cn.ihuoniao.event.base.StoreChangeEvent;
import cn.ihuoniao.platform.webview.CallBackFunction;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatEvent extends StoreChangeEvent {

    public String eventName = "";

    public IWXAPI wxApi = null;

    public CallBackFunction function = null;

    public String wxLoginInfo = "";
}
