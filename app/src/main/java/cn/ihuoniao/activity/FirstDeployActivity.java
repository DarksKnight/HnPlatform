package cn.ihuoniao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.ihuoniao.Constant;
import cn.ihuoniao.R;
import cn.ihuoniao.adapter.FirstDeployPageAdapter;
import cn.ihuoniao.base.BaseFragmentActivity;
import cn.ihuoniao.event.AppConfigEvent;
import cn.ihuoniao.fragment.FirstDeployFragment;
import cn.ihuoniao.platform.viewpagerindicator.CirclePageIndicator;
import cn.ihuoniao.store.AppConfigStore;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class FirstDeployActivity extends BaseFragmentActivity implements View.OnClickListener {

    private List<FirstDeployFragment> listFragment = new ArrayList<>();
    private ViewPager vp = null;
    private TextView tvSkip = null;
    private CirclePageIndicator cpi = null;
    private FragmentPagerAdapter adapter = null;
    private String[] urls = new String[]{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718948173&di=85bc7c9f75a14127280fdeb17325f88d&imgtype=0&src=http%3A%2F%2Fwww.1tong.com%2Fuploads%2Fallimg%2F130806%2F1-130P61045170-L.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718966741&di=f62455c41c820a2095c62008b4626708&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201305%2F04%2F181712hd2hv6atncvqntga.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718990213&di=bc975b26c7ff6a6fce82e5ad328b98ee&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201412%2F09%2F20141209002509_u5hrh.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490313725&di=17c700b87b64afc233b3017dbbde42cd&imgtype=jpg&er=1&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F244%2F64P3023HQL9Z.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489719012472&di=d1f707107e509de492b73a74b8231d4a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2F574f9b1001e93901b32412d17bec54e737d19655.jpg"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_deploy);
        init();
    }

    @Override
    protected void initData() {
        super.initData();

        registerStore(new AppConfigStore());
        actionsCreator.request_getAppConfig();

        adapter = new FirstDeployPageAdapter(getSupportFragmentManager(), listFragment);
        vp.setAdapter(adapter);
        cpi.setViewPager(vp);
        cpi.setRadius(getResources().getDimension(R.dimen.hn_5dp));
    }

    @Override
    protected void initView() {
        super.initView();

        //判断是否第一次启动
        SharedPreferences setting = getSharedPreferences(Constant.HN_SETTING, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (!user_first) {
            startSplashActivity();
        } else {
            setting.edit().putBoolean("FIRST", false).commit();
        }

        vp = getView(R.id.vp_first_deploy);
        tvSkip = getView(R.id.tv_first_deploy_skip);
        cpi = getView(R.id.cpi_first_deploy);

        tvSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvSkip) {
            startMainActivity();
        }
    }

    private void startSplashActivity() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void startMainActivity() {
        if (Constant.APP_INFO.isNeedFinish) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, getString(R.string.toast_init), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onStoreChange(AppConfigEvent event) {
        listFragment.clear();
        for (int i = 0; i < 5; i++) {
            FirstDeployFragment fragment = new FirstDeployFragment();
            fragment.setPicUrl(urls[i]);
            listFragment.add(fragment);
        }
        listFragment.get(listFragment.size() - 1).setLast(true);
        adapter.notifyDataSetChanged();
    }
}
