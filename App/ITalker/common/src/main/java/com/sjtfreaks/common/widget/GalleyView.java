package com.sjtfreaks.common.widget;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sjtfreaks.common.R;
import com.sjtfreaks.common.widget.recycler.RecyclerAdapter;

import java.util.LinkedList;
import java.util.List;


public class GalleyView extends RecyclerView {
    private Adapter mAdapter = new Adapter();
    private static final int MAX_IMAGE_COUNT = 3;//最大选中图片数量
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private static final int LOADER_ID = 0x0100;
    private List<Image> mSelectedImages = new LinkedList<>();
    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     *  内部的数据结构
     */
    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                //cell点击操作，如果点击允许，更新对应的cell的状态
                //更新界面
                if (onItemSelectClick(image)){
                    //noinspection SingleStatementInBlock,unchecked
                    holder.updateData(image);
                }
            }
        });
    }
    //初始化加载方法 返回任何一个ID用于销毁
    public int setup(LoaderManager loaderManager){
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallback);
        return LOADER_ID;

    }
    /**
     * cell 点击的具体逻辑
     * @param image
     * @return true
     */
    private boolean onItemSelectClick(Image image){
        //验证更新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        }else {
            if (mSelectedImages.size()>= MAX_IMAGE_COUNT){
                //通知 TOAST
                notifyRefresh = false;
            }else{
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        //数据有更改 通知监听者
        if (notifyRefresh)
            notifySelectChanged();
        return true;
    }
    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image: mSelectedImages){
            paths[index++] = image.path;

        }
        return paths;
    }
    //进行清空
    public void clear(){
        for (Image image : mSelectedImages){
            //重置状态
            image.isSelect = false;
        }
        mSelectedImages.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
    }
//    通知选中状态改变
    private void notifySelectChanged(){

    }

    /**
     * 用于实际的数据加载
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
    //创建loader
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return null;
        }
//当loader加载完成时
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        }
    //当loader重建时
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private static class Image{
        int id;//数据的ID
        String path;// 图片的路径
        long date;//图片的创建日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Image image = (Image) obj;

            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image>{

        @Override
        protected int getItemViewType(int positon, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }

    }
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
//初始化
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
//绑定
        @Override
        protected void onBind(Image image) {

        }
    }
}
