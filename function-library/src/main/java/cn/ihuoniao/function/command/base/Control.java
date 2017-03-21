package cn.ihuoniao.function.command.base;

import java.util.Map;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public enum  Control {

    INSTANCE;

    private Command command = null;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void doCommand(Map<String, Object> params) {
        command.execute(params);
    }

    public Object doCommand(Command cmd, Map<String, Object> params) {
        return cmd.execute(params);
    }
}
