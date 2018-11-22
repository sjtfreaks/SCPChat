package com.sjtfreaks.italker.push.fragment.media;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtfreaks.common.widget.GalleyView;
import com.sjtfreaks.italker.push.R;

/**
 * 图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleyView.SelectedChangeListener {
    private GalleyView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取我们的galleyview
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleyView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //如果选中一张图片
        if (count>0){
            //隐藏自己
            dismiss();
            if (mListener != null){
                //得到选中的图片的路径
                String[] paths = mGallery.getSelectedPath();
                //返回第一张
                mListener.onSelectedImage(paths[0]);
                mListener = null;
            }
        }
    }
//设置事件监听，并返回自己
    public GalleryFragment setmListener(OnSelectedListener listener){
        mListener = listener;
        return this;
    }

    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }
}
