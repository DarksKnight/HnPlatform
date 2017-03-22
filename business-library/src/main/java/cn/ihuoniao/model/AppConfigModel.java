package cn.ihuoniao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sdk-app-shy on 2017/3/17.
 */

public class AppConfigModel implements Serializable {

    public String cfg_basehost = "";

    public String cfg_webname = "";

    public String cfg_shortname = "";

    public Guide cfg_guide = null;

    public Started cfg_startad = null;

    public Login cfg_loginconnect = null;

    public class Guide {
        public List<String> android = null;
    }

    public class Started {
        public String time = "";

        public String src = "";

        public String link = "";
    }

    public class Login {
        public String qq = null;
    }
}
