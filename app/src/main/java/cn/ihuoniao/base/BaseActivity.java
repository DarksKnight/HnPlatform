package cn.ihuoniao.base;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.ldoublem.loadingviewlib.view.LVCircularRing;

import java.util.Map;

import cn.ihuoniao.R;
import cn.ihuoniao.actions.base.ActionsCreator;
import cn.ihuoniao.dispatcher.Dispatcher;
import cn.ihuoniao.function.command.base.Control;
import cn.ihuoniao.function.util.CommonUtil;
import cn.ihuoniao.model.AppInfoModel;
import cn.ihuoniao.store.base.Store;

/**
 * Created by sdk-app-shy on 2017/3/15.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected Dispatcher dispatcher = Dispatcher.INSTANCE;
    protected ActionsCreator actionsCreator = ActionsCreator.INSTANCE;
    protected AppInfoModel appInfo = AppInfoModel.INSTANCE;
    protected LVCircularRing lvc = null;
    protected Control control = Control.INSTANCE;
    protected boolean isDebug = false;
    protected Map<String, Object> infos = appInfo.infos;

    protected void init() {
        initView();
        registerStores();
        initData();
    }

    protected void initData() {
    }

    protected void initView() {
        isDebug = CommonUtil.isDebug(this);
        CommonUtil.bomb();
        actionsCreator.setParams(infos);
        lvc = new LVCircularRing(this);
        lvc.setBarColor(getResources().getColor(R.color.colorTitle));
        lvc.setLayoutParams(new ViewGroup.LayoutParams((int) getResources().getDimension(R.dimen.hn_50dp), (int) getResources().getDimension(R.dimen.hn_50dp)));
        hideLoading();
    }

    public abstract void registerStores();

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
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                lvc.setVisibility(View.VISIBLE);
                lvc.startAnim();
            }
        });
    }

    protected void hideLoading() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                lvc.setVisibility(View.GONE);
                lvc.stopAnim();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (Map.Entry<String, Store> entry : dispatcher.getStores().entrySet()) {
            entry.getValue().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (Map.Entry<String, Store> entry : dispatcher.getStores().entrySet()) {
            entry.getValue().unregister(this);
        }
    }
}