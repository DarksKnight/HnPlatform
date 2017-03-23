package cn.ihuoniao.function.command.base;

import java.util.Map;

import cn.ihuoniao.function.listener.ResultListener;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public enum  Control {

    INSTANCE;

    private Command command = null;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void doCommand(Map<String, Object> params, ResultListener listener) {
        command.execute(params, listener);
    }

    public void doCommand(Command cmd, Map<String, Object> params, ResultListener listener) {
        cmd.execute(params, listener);
    }
}
