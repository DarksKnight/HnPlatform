package cn.ihuoniao.platform.firstdeploy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ihuoniao.platform.R;
import cn.ihuoniao.platform.firstdeploy.adapter.FirstDeployPageAdapter;
import cn.ihuoniao.platform.firstdeploy.fragment.FirstDeployFragment;
import cn.ihuoniao.platform.viewpagerindicator.CirclePageIndicator;

/**
 * Created by apple on 2017/3/20.
 */

public class FirstDeployView extends LinearLayout {

    private Listener listener = null;
    private List<FirstDeployFragment> listFragment = new ArrayList<>();
    private ViewPager vp = null;
    private TextView tvSkip = null;
    private CirclePageIndicator cpi = null;
    private FragmentPagerAdapter adapter = null;


    public FirstDeployView(Context context) {
        this(context, null, 0);
    }

    public FirstDeployView(Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FirstDeployView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_first_deploy, this);
        initView();
    }

    private void initView() {
        vp = (ViewPager)findViewById(R.id.vp_first_deploy);
        tvSkip = (TextView)findViewById(R.id.tv_first_deploy_skip);
        cpi = (CirclePageIndicator)findViewById(R.id.cpi_first_deploy);

        adapter = new FirstDeployPageAdapter(((FragmentActivity)getContext()).getSupportFragmentManager(), listFragment);
        vp.setAdapter(adapter);
        cpi.setViewPager(vp);
        cpi.setRadius(getResources().getDimension(R.dimen.hn_5dp));

        tvSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.closeView();
            }
        });
    }

    public void setUrls(List<String> urls) {
        listFragment.clear();
        for (int i = 0; i < urls.size(); i++) {
            FirstDeployFragment fragment = new FirstDeployFragment();
            fragment.setPicUrl(urls.get(i));
            listFragment.add(fragment);
        }
        listFragment.get(listFragment.size() - 1).setLast(true);
        listFragment.get(listFragment.size() - 1).setListener(new FirstDeployFragment.Listener() {
            @Override
            public void shouldHide() {
                listener.closeView();
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void closeView();
    }
}
