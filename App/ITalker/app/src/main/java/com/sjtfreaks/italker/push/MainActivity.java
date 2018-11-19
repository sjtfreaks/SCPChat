package com.sjtfreaks.italker.push;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.sjtfreaks.common.Common;
import com.sjtfreaks.common.app.Activity;
import com.sjtfreaks.common.widget.PortraitView;
import com.sjtfreaks.italker.push.fragment.main.ActiveFragment;
import com.sjtfreaks.italker.push.fragment.main.ContactFragment;
import com.sjtfreaks.italker.push.fragment.main.GroupFragment;
import com.sjtfreaks.italker.push.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener,NavHelper.OnTabChangedListener<Integer>{

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitile;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_aciton)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;
//相当于onCreate
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具
        mNavHelper = new NavHelper<>(this,R.id.lay_container,getSupportFragmentManager(),this );
        //调用fragment封装
        mNavHelper.add(R.id.action_home,new NavHelper.Tab<>(ActiveFragment.class,R.string.title_home))
                    .add(R.id.action_group,new NavHelper.Tab<>(GroupFragment.class,R.string.title_group))
                    .add(R.id.action_contact,new NavHelper.Tab<>(ContactFragment.class,R.string.title_contact));
        //回调,底部导航栏的监听
        mNavigation.setOnNavigationItemSelectedListener(this);

        //导航栏背景
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        //接管Menu 手动触发first点击
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home,0);
    }

    @OnClick(R.id.im_searh)
    void onSearchMenuClick(){

    }
    @OnClick(R.id.btn_aciton)
    void onActionClick(){

    }
    boolean isFirst;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //转接事件流到工具类
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

    /**
     * NavHelper回调方法
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //取出title
        mTitile.setText(newTab.extra);
        //添加动画

        float transY = 0;
        float rotation = 0;

        if (Objects.equals(newTab.extra,R.string.title_home)){
            transY = Ui.dipToPx(getResources(),80);
        }else {
            if (Objects.equals(newTab.extra,R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            }else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
            //开始动画
        //旋转Y轴位移 弹性效果
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(480)
                .start();
    }
}
