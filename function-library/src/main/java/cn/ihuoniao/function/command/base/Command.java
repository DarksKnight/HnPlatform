package cn.ihuoniao.function.command.base;

import java.util.Map;

/**
 * Created by sdk-app-shy on 2017/3/20.
 */

public abstract class Command<T extends Receiver> {

    protected T receiver = null;

    public Command(T receiver) {
        this.receiver = receiver;
    }

    public abstract void execute(Map<String, Object> params);
}
