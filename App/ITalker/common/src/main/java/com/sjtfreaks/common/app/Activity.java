package com.sjtfreaks.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在界面初始化调用
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            //设置界面ID
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    protected void initWindows(){

    }


    //初始化相关参数
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    //得到当前界面的资源Id
    protected abstract int getContentLayoutId();

    //初始化控件
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前Activity所有fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size()>0){
            for (Fragment fragment : fragments){
                //判断是否为我们能处理的类型
                if (fragment instanceof com.sjtfreaks.common.app.Fragment){
                    //判断是否拦截了返回按钮
                    if (((com.sjtfreaks.common.app.Fragment) fragment).OnBackPressed()){
                        //有！直接返回
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
