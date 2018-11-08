package com.sjtfreaks.italker.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtfreaks.common.Common;
import com.sjtfreaks.common.app.Activity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements IView {
    @BindView(R.id.tv_result)
    TextView mResultText;

    @BindView(R.id.et_query)
    EditText mInputText;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    private IPresenter mPresenter;

//相当于onCreate
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

//    @Override
//    protected void initWidget() {
//        super.initWidget();
//    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new Presenter(this);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(){
        mPresenter.search();
    }

    @Override
    public String getInputString() {
        return mInputText.getText().toString();
    }

    @Override
    public void setResultString(String string) {
        mResultText.setText(string);
    }
}
