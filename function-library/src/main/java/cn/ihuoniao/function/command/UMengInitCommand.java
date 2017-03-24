package cn.ihuoniao.function.command;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.function.command.base.Command;
import cn.ihuoniao.function.listener.ResultListener;
import cn.ihuoniao.function.receiver.UMengInitReceiver;

/**
 * Created by sdk-app-shy on 2017/3/24.
 */

public class UMengInitCommand extends Command<Object, UMengInitReceiver> {

    public UMengInitCommand(UMengInitReceiver receiver) {
        super(receiver);
    }

    @Override
    public void execute(Map<String, Object> params, ResultListener<Object> listener) {
        Map<String, String> infos = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            infos.put(entry.getKey(), entry.getValue().toString());
        }
        receiver.init((Activity)params.get("context"), infos);
    }
}
