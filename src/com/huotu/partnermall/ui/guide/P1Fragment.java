package com.huotu.partnermall.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huotu.partnermall.inner.R;

/**
 * Created by Administrator on 2015/10/12.
 */
public class P1Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate( R.layout.guide_item_ui_page1, container, false);
        return view;
    }

    @Override
    public
    void onViewCreated ( View view, Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );
    }
}
