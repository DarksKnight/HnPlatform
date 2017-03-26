package cn.ihuoniao.actions;

import java.util.Map;

import cn.ihuoniao.actions.base.BaseAction;

/**
 * Created by apple on 2017/3/26.
 */

public class AlipayAction extends BaseAction<Map<String, Object>> {

    public AlipayAction(String type, Map<String, Object> data) {
        super(type, data);
    }
}
