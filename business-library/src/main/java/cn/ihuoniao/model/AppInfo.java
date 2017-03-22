package cn.ihuoniao.model;

import java.io.Serializable;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public enum  AppInfo implements Serializable {
    INSTANCE;

    public boolean isLoadFinish = false;

    public String platformUrl = "";

    public AppConfigModel.Login loginInfo = null;
}
