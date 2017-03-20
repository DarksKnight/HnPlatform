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
    private String[] urls = new String[]{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718948173&di=85bc7c9f75a14127280fdeb17325f88d&imgtype=0&src=http%3A%2F%2Fwww.1tong.com%2Fuploads%2Fallimg%2F130806%2F1-130P61045170-L.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718966741&di=f62455c41c820a2095c62008b4626708&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201305%2F04%2F181712hd2hv6atncvqntga.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489718990213&di=bc975b26c7ff6a6fce82e5ad328b98ee&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201412%2F09%2F20141209002509_u5hrh.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490313725&di=17c700b87b64afc233b3017dbbde42cd&imgtype=jpg&er=1&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F244%2F64P3023HQL9Z.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489719012472&di=d1f707107e509de492b73a74b8231d4a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2F574f9b1001e93901b32412d17bec54e737d19655.jpg"};

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

    public void setUrls() {
        listFragment.clear();
        for (int i = 0; i < 5; i++) {
            FirstDeployFragment fragment = new FirstDeployFragment();
            fragment.setPicUrl(urls[i]);
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
