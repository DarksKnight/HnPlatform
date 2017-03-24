package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import cn.ihuoniao.function.command.base.Receiver;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class UMengShareReceiver extends Receiver {

    public void shareUmeng(Activity activity, String title, String summary, String url, String imageUrl, UMShareListener listener) {
        ShareAction shareAction = new ShareAction(activity);
        UMImage image = new UMImage(activity, imageUrl);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(image);
        web.setDescription(summary);
        if (title.trim().length() == 0) {
            shareAction.withMedia(image);
        } else {
            shareAction.withMedia(web);
        }
        shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.setCallback(listener);
        Config.DEBUG = true;
        shareAction.open();
    }
}
