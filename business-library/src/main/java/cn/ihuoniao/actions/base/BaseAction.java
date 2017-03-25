package cn.ihuoniao.actions.base;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public abstract class BaseAction<T> {
    private final String type;
    private final T data;

    public BaseAction(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
