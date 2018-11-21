package com.sjtfreaks.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sjtfreaks.common.app.Activity;
import com.sjtfreaks.italker.push.R;
import com.sjtfreaks.italker.push.fragment.account.UpdateInfoFragment;

public class AccountActivity extends Activity {
    /**
     * 账户activity显示入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,new UpdateInfoFragment())
                .commit();
    }
}
