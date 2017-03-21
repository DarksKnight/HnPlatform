package cn.ihuoniao.store.base;

import com.squareup.otto.Bus;

import cn.ihuoniao.actions.base.BaseAction;
import cn.ihuoniao.event.base.StoreChangeEvent;
import cn.ihuoniao.function.command.base.Control;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public abstract class Store<T extends BaseAction> {

    private static final Bus bus = new Bus();

    protected Control control = Control.INSTANCE;

    protected Store() {
    }

    public void register(final Object view) {
        this.bus.register(view);
    }

    public void unregister(final Object view) {
        this.bus.unregister(view);
    }

    protected void emitStoreChange(StoreChangeEvent event) {
        this.bus.post(event);
    }

    public abstract void onAction(T action);
}
