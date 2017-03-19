package cn.ihuoniao.base;

import com.jaeger.library.StatusBarUtil;

import android.app.Activity;
import android.view.View;

import cn.ihuoniao.R;
import cn.ihuoniao.actions.base.ActionsCreator;
import cn.ihuoniao.dispatcher.Dispatcher;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/15.
 */

public abstract class BaseActivity extends Activity {

    protected Dispatcher dispatcher = Dispatcher.INSTANCE;
    protected ActionsCreator actionsCreator = ActionsCreator.INSTANCE;
    protected boolean isSetStatusColor = true;

    protected void init() {
        if (isSetStatusColor) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.colorTitle));
        }

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

    protected void registerStore(Store store) {
        dispatcher.register(store);
    }

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