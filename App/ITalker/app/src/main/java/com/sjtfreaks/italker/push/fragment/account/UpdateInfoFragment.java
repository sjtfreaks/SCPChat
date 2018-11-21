package com.sjtfreaks.italker.push.fragment.account;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtfreaks.common.app.Fragment;
import com.sjtfreaks.common.widget.PortraitView;
import com.sjtfreaks.italker.push.R;

import butterknife.BindView;
import butterknife.OnClick;

//用户更新信息
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }
//点击
    @OnClick(R.id.im_portrait)
    void onPortraitClick(){

    }
}
