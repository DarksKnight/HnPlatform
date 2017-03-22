package cn.ihuoniao.event;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import cn.ihuoniao.event.base.StoreChangeEvent;

/**
 * Created by sdk-app-shy on 2017/3/21.
 */

public class QQEvent extends StoreChangeEvent {

    public String eventName = "";
    public Tencent tencent = null;
    public IUiListener listener = null;
}
