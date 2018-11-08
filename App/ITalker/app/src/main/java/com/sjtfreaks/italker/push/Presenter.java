package com.sjtfreaks.italker.push;

import android.text.TextUtils;

public class Presenter implements IPresenter {

    private IView mView;

    public Presenter(IView view){
        mView = view;
    }

    @Override
    public void search() {
        //open loading
        String inputString = mView.getInputString();
        if (TextUtils.isEmpty(inputString)){
            //为null,直接返回
            return;
        }
        int hashCode = inputString.hashCode();
        IUserService service = new UserService();
        String serviceResult = service.search(hashCode);
        String result = "Result: " + inputString+ "-" + serviceResult;

        //逻辑处理 关闭Ldoing

        mView.setResultString(result);
    }

}
