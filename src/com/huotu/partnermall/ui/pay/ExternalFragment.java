package com.huotu.partnermall.ui.pay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huotu.partnermall.inner.R;

/**
 * 支付宝钱包支付
 */
public
class ExternalFragment extends Fragment {

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.pay_external, container, false);
    }
}