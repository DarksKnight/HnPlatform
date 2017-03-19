package cn.ihuoniao.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.ihuoniao.R;
import cn.ihuoniao.activity.MainActivity;

/**
 * Created by sdk-app-shy on 2017/3/16.
 */

public class FirstDeployFragment extends Fragment implements View.OnClickListener {

    private SimpleDraweeView ivPic = null;
    private String url = "";
    private boolean isLast = false;
    private float oldX = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_deploy, container, false);

        ivPic = (SimpleDraweeView) rootView.findViewById(R.id.iv_first_deploy_pic);
        Uri uri = Uri.parse(url);
        ivPic.setImageURI(uri);
        ivPic.setOnClickListener(this);

        ivPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (oldX == -1) {
                    oldX = event.getX();
                }
                if (isLast) {
                    if (oldX > event.getX()) {
                        startMainActivity();
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    public void setPicUrl(String url) {
        this.url = url;
    }

    public void setLast(boolean flag) {
        isLast = flag;
    }

    @Override
    public void onClick(View v) {
        if (v == ivPic) {
            if (isLast) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
