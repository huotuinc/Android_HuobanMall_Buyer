package com.huotu.partnermall.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.huotu.partnermall.ui.frags.FragmentInteractionListener;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/4/12.
 */
public class BaseFragment extends Fragment {
    //private FragmentInteractionListener mListener;
    /**
     * 标记 是否 主界面
     */
    protected boolean isMainUI = false;

    public boolean isMainUI() {
        return isMainUI;
    }

    public void setMainUI(boolean mainUI) {
        isMainUI = mainUI;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void Register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void UnRegister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void setUrl(String url ){}

    public void setParameter(String smartUrl , int classId , String keyword){}

    public void refreshTitle(){}

    public void share(){}
}
