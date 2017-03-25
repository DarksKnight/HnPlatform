package cn.ihuoniao.function.receiver;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public class QQShareReceiver extends Receiver {

    public void shareQQ(Tencent tencent, Activity activity, String title, String summary, String url, String imageUrl, String appName, IUiListener listener) {
        final Bundle params = new Bundle();
        //标题为空的时候，分享纯图片，不为空，分享图文
        if (title.trim().length() == 0) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        tencent.shareToQQ(activity, params, listener);
    }

    public void shareQQZone (Tencent tencent, Activity activity, String title, String summary, String url, ArrayList<String> imageUrls, IUiListener listener) {
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT + "");
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        tencent.shareToQzone(activity, params, listener);
    }
}
