package com.sjtfreaks.italker.push.fragment.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtfreaks.common.app.Fragment;
import com.sjtfreaks.common.widget.GalleyView;
import com.sjtfreaks.italker.push.R;

import butterknife.BindView;


public class ActiveFragment extends Fragment {
    @BindView(R.id.galleyView)
    GalleyView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
