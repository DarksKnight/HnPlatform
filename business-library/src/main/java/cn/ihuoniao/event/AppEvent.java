package cn.ihuoniao.event;

import cn.ihuoniao.event.base.StoreChangeEvent;
import cn.ihuoniao.model.AppConfigModel;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppEvent extends StoreChangeEvent {

    public AppConfigModel appConfig = null;

}
