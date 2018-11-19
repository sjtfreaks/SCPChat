package com.sjtfreaks.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sjtfreaks.common.R;
import com.sjtfreaks.common.widget.recycler.RecyclerAdapter;


public class GalleyView extends RecyclerView {
    private Adapter mAdapter = new Adapter();

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

    /**
     * cell
     * @param image
     * @return true
     */
    private boolean onItemSelectClick(Image image){

        return true;
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
