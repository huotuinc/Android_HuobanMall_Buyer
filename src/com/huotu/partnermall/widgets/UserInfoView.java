package com.huotu.partnermall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.adapter.UserInfoAdapter;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.UserSelectData;
import com.huotu.partnermall.utils.DensityUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 */
public
class UserInfoView {

    public enum Type
    {
        Name, Sex, Age, Job, Income, Fav
    }

    public HashMap< Type, String > titleNames = new HashMap< UserInfoView.Type, String > ( );

    public
    interface OnUserInfoBackListener {
        void onUserInfoBack ( Type type, UserSelectData data );
    }

    private OnUserInfoBackListener listener;

    private View mainView;
    private BaseApplication
                 application;

    private TextView txtTitle;

    private ListView listView;

    private Context mContext;

    private TextView btnSure;

    private Dialog dialog;

    private LinearLayout layMain;

    private EditText edtName;

    public
    UserInfoView ( Context context, BaseApplication application ) {
        this.mContext = context;
        this.application = application;
        initView ( context );
    }

    public
    void setOnUserInfoBackListener ( OnUserInfoBackListener listener ) {
        this.listener = listener;
    }

    private
    void initView ( Context context ) {
        if ( dialog == null ) {
            mainView = LayoutInflater.from ( context ).inflate (
                    R.layout.pop_userinfo, null
                                                               );
            dialog = new Dialog ( context, R.style.PopDialog );
            dialog.setContentView ( mainView );
            Window window = dialog.getWindow ( );
            window.setGravity( Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.AnimationPop); // 添加动画

            // 设置视图充满屏幕宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            int[] size = DensityUtils.getSize ( mContext );
            lp.width = size[0]; // 设置宽度
            // lp.height = (int) (size[1]*0.8);
            window.setAttributes(lp);
        }
        titleNames.put(Type.Name, "姓名");
        titleNames.put(Type.Sex, "性别");
        titleNames.put(Type.Age, "出生年份");
        titleNames.put(Type.Job, "职业");
        titleNames.put(Type.Income, "收入");
        titleNames.put(Type.Fav, "爱好");
        edtName = (EditText) mainView.findViewById(R.id.edtName);
        txtTitle = (TextView) mainView.findViewById(R.id.txtTitle);
        listView = (ListView) mainView.findViewById(R.id.listView);

        btnSure = (TextView) mainView.findViewById(R.id.btnSure);
        btnSure.setOnClickListener(new View.OnClickListener()
                                   {

                                       // 在确定后逐个提交修改信息
                                       @Override
                                       public void onClick(View v)
                                       {
                                           if (curType == Type.Name)
                                           {
                    /*
                     * if(listener != null){ listener.onUserInfoBack(Type.Name,
                     * new UserSelectData(edtName.getText().toString(), "0"));
                     *
                     * }
                     */
                                               //new modifyUserInfoAsyncTask().execute();

                                           } else if (curType == Type.Sex)
                                           {
                    /*
                     * List<UserSelectData> result = adapter.getSelectData();
                     *
                     *
                     * StringBuffer tag = new StringBuffer(); StringBuffer name
                     * = new StringBuffer(); int length = result.size(); for
                     * (int i = 0; i < length; i++) {
                     * tag.append(result.get(i).id);
                     * name.append(result.get(i).name); if(i != length -1){
                     * tag.append(","); name.append(","); }
                     *
                     * } if(listener != null){ listener.onUserInfoBack(Type.Fav,
                     * new UserSelectData(name.toString(), tag.toString())); }
                     */
                                               //new modifyUserInfoAsyncTask().execute();
                                           } else if (curType == Type.Job)
                                           {
                                               //new modifyUserInfoAsyncTask().execute();
                                           } else if (curType == Type.Income)
                                           {
                                               //new modifyUserInfoAsyncTask().execute();
                                           } else if (curType == Type.Fav)
                                           {
                                               //new modifyUserInfoAsyncTask().execute();
                                           }

                                       }
                                   });
        // init support
        mainView.findViewById(R.id.btnCancel).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (listener != null)
                            listener.onUserInfoBack(null, null);
                        dialog.dismiss();
                    }
                });

        mainView.setFocusableInTouchMode(true);
        mainView.setOnKeyListener(new View.OnKeyListener()
                                  {
                                      @Override
                                      public boolean onKey(View v, int keyCode, KeyEvent event)
                                      {
                                          if (keyCode == KeyEvent.KEYCODE_BACK)
                                          {
                                              if (listener != null)
                                                  listener.onUserInfoBack(null, null);
                                              dialog.dismiss();
                                          }
                                          return false;
                                      }
                                  });
        layMain = (LinearLayout) mainView.findViewById(R.id.layMain);

    }

    private Handler handler = new Handler ( );

    private UserInfoAdapter adapter;

    private Type curType;

    public
    void show (
            final Type type, final List< UserSelectData > datas,
            String selectIds
              ) {
        curType = type;
        txtTitle.setText ( titleNames.get ( type ) );
        // btnSure.setVisibility(type == Type.Fav || type == Type.Name ?
        // View.VISIBLE : View.GONE);
        edtName.setVisibility ( type == Type.Name ? View.VISIBLE : View.GONE );
        listView.setVisibility ( type == Type.Name ? View.GONE : View.VISIBLE );
        dialog.show ( );
        if ( type == Type.Name ) {
            edtName.requestFocus ( );
            edtName.requestFocusFromTouch ( );

            final InputMethodManager imm = ( InputMethodManager ) mContext
                    .getSystemService ( Context.INPUT_METHOD_SERVICE );
            handler.postDelayed (
                    new Runnable ( ) {
                        @Override
                        public
                        void run ( ) {
                            imm.toggleSoftInput ( 0, InputMethodManager.HIDE_NOT_ALWAYS );
                        }
                    }, 10
                                );
            edtName.setText ( selectIds );

        }
        else {
            adapter = new UserInfoAdapter ( mContext, datas );
            listView.setAdapter ( adapter );
            if ( ! TextUtils.isEmpty ( selectIds ) ) {
                // boolean[] tags = new boolean[datas.size()];
                for ( int i = 0, length = datas.size ( ) ; i < length ; i++ ) {
                    if ( selectIds.contains ( datas.get ( i ).id ) )
                        adapter.setSelect ( i );
                }
            }
            listView.setOnItemClickListener (
                    new AdapterView.OnItemClickListener ( ) {

                        @Override
                        public
                        void onItemClick (
                                AdapterView< ? > arg0, View arg1,
                                int arg2, long arg3
                                         ) {
                            if ( type == Type.Sex || type == Type.Job
                                 || type == Type.Income ) {
                                adapter.setSelectOne ( arg2 );
                                if ( listener != null ) {
                                    listener.onUserInfoBack ( type, datas.get ( arg2 ) );
                                }
                                // dialog.dismiss();
                            }
                            else if ( type == Type.Fav ) {
                                adapter.setSelect ( arg2 );
                            }

                        }
                    }
                                            );
        }

        // set height
        LinearLayout.LayoutParams params = ( LinearLayout.LayoutParams ) layMain
                .getLayoutParams ( );
        // reset
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;// ownHeight >
        // height ?
        // height
        // :ownHeight;
        layMain.setLayoutParams ( params );

        ViewTreeObserver vto2 = layMain.getViewTreeObserver ( );
        vto2.addOnGlobalLayoutListener (
                new ViewTreeObserver.OnGlobalLayoutListener ( ) {
                    @Override
                    public
                    void onGlobalLayout ( ) {
                        layMain.getViewTreeObserver ( )
                               .removeGlobalOnLayoutListener ( this );
                        LinearLayout.LayoutParams params = ( LinearLayout.LayoutParams ) layMain
                                .getLayoutParams ( );
                        int ownHeight = layMain.getHeight ( );
                        int height    = ( int ) ( DensityUtils.getSize ( mContext )[ 1 ] * 0.75 );
                        params.height = ownHeight > height ? height : ownHeight;
                        layMain.setLayoutParams ( params );
                    }
                }
                                       );

    }


    public String obtainUserData(String key, String type)
    {
        String result = null;
        if ("job".equals(type))
        {
            // 从application中获取职业信息，初始化接口获取
            // 匹配key获取职业
           /* Value[] jobs = application.globalData.getCareer();
            for(Value job:jobs)
            {
                if(String.valueOf(job.getValue()).equals(key))
                {
                    result = job.getName();
                }
            }*/

        } else if ("Fav".equals(type))
        {
            // 从application中获取爱好信息，初始化接口获取
            // 匹配key获取爱好
            /*Value[] favs = application.globalData.getFavs();
            StringBuilder builder = new StringBuilder();
            String[] keys = key.split(",");
            for(int i=0; i<keys.length; i++)
            {
                for(Value fav:favs)
                {
                    if(String.valueOf(fav.getValue()).equals(keys[i]))
                    {
                        builder.append(fav.getName());
                        builder.append(",");
                    }
                }
            }
            result = builder.toString().substring(0, (builder.toString().length()-1));*/

        } else if ("Income".equals(type))
        {
            // 从application中获取收入信息，初始化接口获取
            // 匹配key获取收入
            /*Value[] incomings = application.globalData.getIncomings();
            for(Value incoming:incomings)
            {
                if(String.valueOf(incoming.getValue()).equals(key))
                {
                    result = incoming.getName();
                }
            }*/
        } else if ("Sex".equals(type))
        {
            if ("0".equals(key))
            {
                result = "男";
            } else if ("1".equals(key))
            {
                result = "女";
            }
        }

        return result;
    }
}
