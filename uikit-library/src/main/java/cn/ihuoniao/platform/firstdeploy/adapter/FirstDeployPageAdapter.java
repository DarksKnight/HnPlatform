package cn.ihuoniao.platform.firstdeploy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.ihuoniao.platform.firstdeploy.fragment.FirstDeployFragment;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class FirstDeployPageAdapter extends FragmentPagerAdapter {

    private List<FirstDeployFragment> listFragment = null;

    public FirstDeployPageAdapter(FragmentManager fm, List<FirstDeployFragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
