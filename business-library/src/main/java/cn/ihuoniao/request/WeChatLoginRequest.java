package cn.ihuoniao.request;

import java.util.Map;

import cn.ihuoniao.request.base.BaseRequest;
import cn.ihuoniao.request.base.Request;
import cn.ihuoniao.request.base.RequestCallBack;

/**
 * Created by sdk-app-shy on 2017/3/23.
 */

public class WeChatLoginRequest extends BaseRequest<Map<String, String>> {

    @Override
    public void request(Map<String, String> params, RequestCallBack callback) {
        Request.post("https://api.weixin.qq.com/sns/oauth2/access_token", params, callback);
    }
}
