package cn.ihuoniao.store;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.Event;
import cn.ihuoniao.TYPE;
import cn.ihuoniao.actions.AlipayAction;
import cn.ihuoniao.function.command.AlipayCommand;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.AlipayReceiver;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.model.PayInfoModel;
import cn.ihuoniao.platform.webview.BridgeHandler;
import cn.ihuoniao.platform.webview.CallBackFunction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by apple on 2017/3/26.
 */

public class AlipayStore extends Store<AlipayAction> {

    @Override
    public void onAction(AlipayAction action) {
        super.onAction(action);
        switch (action.getType()) {
            case TYPE.TYPE_ALIPAY_PAY:
                pay();
                break;
            default:
                break;
        }
    }

    private void pay() {
        webView.registerHandler(Event.PAY_ALIPAY, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                statusListener.start();
                PayInfoModel payInfo = JSON.parseObject(data, PayInfoModel.class);
                Map<String, Object> params = new HashMap<>();
                params.put("activity", activity);
                params.put("orderInfo", payInfo.orderInfo);
                control.doCommand(new AlipayCommand(new AlipayReceiver()), params,
                        new ResultListener<String>() {
                            @Override
                            public void onResult(String result) {
                                statusListener.end();
                                Logger.i("alipay result : " + result);
                                function.onCallBack(result);
                            }
                        });
            }
        });
    }
}
