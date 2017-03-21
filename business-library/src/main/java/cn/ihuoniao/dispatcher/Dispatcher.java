package cn.ihuoniao.dispatcher;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.actions.base.BaseAction;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public enum  Dispatcher {
    INSTANCE;

    private final Map<String, Store> stores = new HashMap<>();

    public void register(String key, final Store store) {
        stores.put(key, store);
    }

    public void unregister(final Store store) {
        stores.remove(store);
    }

    public void dispatch(String key, BaseAction action) {
        stores.get(key).onAction(action);
    }

    public Map<String, Store> getStores() {
        return stores;
    }
}
