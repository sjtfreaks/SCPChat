package com.sjtfreaks.common.widget;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sjtfreaks.common.R;
import com.sjtfreaks.common.widget.recycler.RecyclerAdapter;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class GalleyView extends RecyclerView {
    private Adapter mAdapter = new Adapter();
    private static final int MAX_IMAGE_COUNT = 3;//最大选中图片数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//最小选中图片大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private static final int LOADER_ID = 0x0100;
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mListener;

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
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener){
        mListener = listener;
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
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str,MAX_IMAGE_COUNT);
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
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
        SelectedChangeListener listener = mListener;
        if (listener != null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }
//刷新 通知数据更改的方法
    private void updateSource(List<Image> images){
        mAdapter.replace(images);
    }
    /**
     * 用于实际的数据加载
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,//id
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_ADDED//图片创建time
        };
    //创建loader
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

            if (id == LOADER_ID){
                //是我们的id，进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2]+ " DESC");//倒序查询
            }
            return null;
        }

        //当loader加载完成时
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Image> images = new ArrayList<>();
            //判断是否有数据
            if (data != null){
                int count = data.getCount();
                if (count > 0){
                    //移动游标到底部
                    data.moveToFirst();
                    //得到对应坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        //循环读取
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists()|| file.length() < MIN_IMAGE_FILE_SIZE){
                            //没有图片或图片太小
                            continue;
                        }
                        //添加新的数据
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);

                    }while (data.moveToNext());
                }
            }
            updateSource(images);
        }
    //当loader重建时
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //界面清空
            updateSource(null);
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
//适配器
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
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;
//初始化
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
        }
//绑定
        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//原图加载
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);

            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    public interface SelectedChangeListener{
        void onSelectedCountChanged(int count);
    }
}
