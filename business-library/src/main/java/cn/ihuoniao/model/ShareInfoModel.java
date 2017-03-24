package cn.ihuoniao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class ShareInfoModel implements Serializable {

    public String title = "";

    public String summary = "";

    public String url = "";

    public String imageUrl = "";

    public List<String> imageUrls = new ArrayList<>();
}
