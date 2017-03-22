package cn.ihuoniao.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.ihuoniao.actions.base.ActionsCreator;
import cn.ihuoniao.dispatcher.Dispatcher;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class BaseFragmentActivity extends FragmentActivity {

    protected List<Store> listStore = new ArrayList<>();
    protected Dispatcher dispatcher = Dispatcher.INSTANCE;
    protected ActionsCreator actionsCreator = ActionsCreator.INSTANCE;

    protected void init() {
        initView();
        initData();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected final <E extends View> E getView(int id) {
        return (E) findViewById(id);
    }

//    protected void registerStore(Store store) {
//        dispatcher.register(store);
//    }

    protected void unregisterStore(Store store) {
        dispatcher.unregister(store);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for(int i = 0;i < dispatcher.getStores().size(); i++) {
            dispatcher.getStores().get(i).register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for(int i = 0;i < dispatcher.getStores().size(); i++) {
            dispatcher.getStores().get(i).unregister(this);
        }
    }
}
