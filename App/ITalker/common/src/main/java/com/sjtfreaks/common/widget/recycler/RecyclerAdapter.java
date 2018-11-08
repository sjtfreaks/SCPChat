package com.sjtfreaks.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtfreaks.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,AdapterCallback<Data>{

    private  final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    //构造函数模块
    public RecyclerAdapter(){
        this(null);
    }
    public RecyclerAdapter(AdapterListener<Data> listener ){
        this(new ArrayList<Data>(),listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener ){
        this.mDataList = dataList;
        this.mListener = listener;
    }

    //复习默认的布局类型返回,得到 坐标position 数据data
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position,mDataList.get(position));
    }
    @LayoutRes
    protected abstract int getItemViewType(int positon, Data data);

    //创建一个ViewHoder

    @NonNull
    @Override
    //创建 viewType 预定为Xml ID
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(viewType,viewGroup,false);
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        //设置View的tag为viewholder进行双相绑定
        root.setTag(R.id.tag_recycler_holder,holder);
        //设置点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder,root);
        //绑定callback
        holder.callback = this;

        return holder;
    }

    //new 的ViewHolder
    //viewType 布局类型，其实就是XML的ID，返回ViewHolder


    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);
    //绑定数据到一个dataViewHoder上
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        //触发holder绑定
        holder.bind(data);

    }
    //获取长度 得到当前集合的数据量
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    //插入并通知一堆数据，并通知集合更新

    protected void add(Data data){
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }
    public void add(Data... dataList){
        if (dataList != null&&dataList.length > 0 ){
            int startPos = mDataList.size();
            Collections.addAll(mDataList,dataList);
            notifyItemRangeInserted(startPos,dataList.length);
        }
    }
    //插入并通知一堆数据，并通知集合更新
    public void add(Collection<Data> dataList){
        if (dataList != null&&dataList.size() > 0 ){
            int startPos = mDataList.size();
           mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos,dataList.size());
        }
    }
    //清空数据并更新
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }
    //修改数据 替换集合包括了清空
    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if (dataList == null || dataList.size() == 0){
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemClick(viewHolder,mDataList.get(pos));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemClick(viewHolder,mDataList.get(pos));
        }
    }
    //设置适配器监听
    public void setListener(AdapterListener<Data> adapterListener){
        this.mListener = adapterListener;
    }

    //监听器 泛型data
    public interface AdapterListener<Data>{
        //当cell 触发
        void onItemClick(RecyclerAdapter.ViewHolder holder,Data data);
        //当cell 长按触发
        void OnItemLongClick(RecyclerAdapter.ViewHolder holder,Data data);
    }

    //自定义ViewHolder 繁星data
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        private Unbinder unbinder;
        protected Data mData;
        private AdapterCallback<Data> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        //绑定的数据触发
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }
        //绑定数据时的回调，必须复写
        protected abstract void onBind(Data data);

        //holder自我更新操作

        public void updateData(Data data){
        if (this.callback != null){
            this.callback.update(data,this);
        }
        }
    }
}
