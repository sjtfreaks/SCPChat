package com.sjtfreaks.common.widget.recycler;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;

import java.util.Date;

public interface AdapterCallback<Data> {
    //update data
    void update (Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
