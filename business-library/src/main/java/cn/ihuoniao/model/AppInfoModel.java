package cn.ihuoniao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public enum AppInfoModel implements Serializable {
    INSTANCE;

    public boolean isLoadFinish = false;

    public String platformUrl = "";

    public AppConfigModel.Login loginInfo = null;

    public Map<String, Object> infos = new HashMap<>();

    public String pushStatus = "on";

    public String pushToken = "";

    public boolean isFirstRun = true;
}
