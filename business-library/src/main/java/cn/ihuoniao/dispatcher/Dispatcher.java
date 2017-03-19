package cn.ihuoniao.dispatcher;

import java.util.ArrayList;
import java.util.List;

import cn.ihuoniao.actions.base.BaseAction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public enum  Dispatcher {
    INSTANCE;

    private final List<Store> stores = new ArrayList<>();

    public void register(final Store store) {
        if (!stores.contains(store)) {
            stores.add(store);
        }
    }

    public void unregister(final Store store) {
        stores.remove(store);
    }

    public void dispatch(BaseAction action) {
        post(action);
    }

    private void post(final BaseAction action) {
        for (Store store : stores) {
            store.onAction(action);
        }
    }

    public List<Store> getStores() {
        return stores;
    }
}
