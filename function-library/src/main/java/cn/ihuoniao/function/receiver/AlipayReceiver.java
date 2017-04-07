package cn.ihuoniao.function.receiver;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.listener.ResultListener;

/**
 * Created by apple on 2017/3/26.
 */

public class AlipayReceiver extends Receiver {

    public void pay(final Activity activity, final String orderInfo,
            final ResultListener<String> listener) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                // 判断resultStatus 为9000则代表支付成功
                if (result.get("resultStatus").equals("9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    //success
                    listener.onResult(result.get("result").toString());
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    //fail
                    listener.onResult("fail");
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public class PayResult {

        private String resultStatus;

        private String result;

        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

}
