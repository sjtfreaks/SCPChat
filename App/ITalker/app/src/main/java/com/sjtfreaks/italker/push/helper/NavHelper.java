package com.sjtfreaks.italker.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;



//fragment调度问题
public class NavHelper<T> {
    //all tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray();

    //初始化参数
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;
    //当前选中的tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加tab
     * @param menuId
     * @param tab
     */
    public NavHelper<T> add(int menuId,Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前的显示Tab
     * @return
     */
    public Tab<T> getCurrentTab(){

        return currentTab;
    }

    //执行点击菜单操作
    public boolean performClickMenu(int menuId) {
//        集合中寻找对应的tab
        Tab<T> tab = tabs.get(menuId);
        if (tab != null){

            return true;
        }
        return false;
    }

    /**
     * 进行真实的处理
     * @param tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if (currentTab != null){
            oldTab = currentTab;
            if (oldTab == tab){
                //当前tab是点击tab
                notifyTabReselect(tab);
                return;
            }
        }
        //赋值调用切换方法
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    /**
     * fragment调度
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (oldTab != null){
            if (oldTab.fragment != null){
                //界面移除，缓存true
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab != null){
            if (newTab.fragment == null){
                //first new
                Fragment fragment = Fragment.instantiate(context,newTab.clx.getName(),null);
                //缓存
                newTab.fragment = fragment;
                ft.add(containerId,fragment,newTab.clx.getName());

            }else {
                //从fragment的缓存空间加载到界面
                ft.attach(newTab.fragment);
            }
        }
        //提交
        ft.commit();
        //通知回调
        notifyTabSelect(newTab,oldTab);
    }
    //回调监听器
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab){
        if (listener != null){
            listener.onTabChanged(newTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab){
        //TODO 二次点击Tab所做操作
    }
    /**
     * 我们的所有的Tab基础属性
     */
    public static class Tab<T>{

        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //        fragment信息
        public Class<?> clx;
//        额外的data
        public T extra;
//        内部缓存fragment package私有！
Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
