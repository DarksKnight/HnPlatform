package cn.ihuoniao.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.ldoublem.loadingviewlib.view.LVCircularRing;

import java.util.Iterator;
import java.util.Map;

import cn.ihuoniao.R;
import cn.ihuoniao.actions.base.ActionsCreator;
import cn.ihuoniao.dispatcher.Dispatcher;
import cn.ihuoniao.function.command.base.Control;
import cn.ihuoniao.model.AppInfo;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/15.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected Dispatcher dispatcher = Dispatcher.INSTANCE;
    protected ActionsCreator actionsCreator = ActionsCreator.INSTANCE;
    protected AppInfo appInfo = AppInfo.INSTANCE;
    protected LVCircularRing lvc = null;
    protected Control control = Control.INSTANCE;

    protected void init() {
        initView();
        registerStores();
        initData();
    }

    protected void initData() {
    }

    protected void initView() {
        lvc = new LVCircularRing(this);
        lvc.setBarColor(getResources().getColor(R.color.colorTitle));
        lvc.setLayoutParams(new ViewGroup.LayoutParams((int) getResources().getDimension(R.dimen.hn_50dp), (int) getResources().getDimension(R.dimen.hn_50dp)));
        hideLoading();
    }

    protected final <E extends View> E getView(int id) {
        return (E) findViewById(id);
    }

    protected void registerStore(String key, Store store) {
        dispatcher.register(key, store);
    }

    protected void unregisterStore(Store store) {
        dispatcher.unregister(store);
    }

    protected void showLoading() {
        lvc.setVisibility(View.VISIBLE);
        lvc.startAnim();
    }

    protected void hideLoading() {
        lvc.setVisibility(View.GONE);
        lvc.stopAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Iterator<Map.Entry<String, Store>> iter = dispatcher.getStores().entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, Store> entry = iter.next();
            entry.getValue().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Iterator<Map.Entry<String, Store>> iter = dispatcher.getStores().entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, Store> entry = iter.next();
            entry.getValue().unregister(this);
        }
    }

    public abstract void registerStores();
}