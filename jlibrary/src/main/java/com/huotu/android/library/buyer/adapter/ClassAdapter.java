package com.huotu.android.library.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.huotu.android.library.buyer.bean.BizBean.ClassBean;
import com.huotu.android.library.buyer.utils.ViewHolderUtil;
import java.util.List;
import com.huotu.android.library.buyer.R;

/**
 * Created by Administrator on 2016/2/2.
 */
public class ClassAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private List<ClassBean> data;
    private Context context;
    private GridView gridView;
    private LayoutInflater inflater;
    private int itemHeight;

    public ClassAdapter( GridView gridView, List<ClassBean> data,Context context) {
        this.data=data;
        this.context = context;
        this.gridView= gridView;
        this.inflater = LayoutInflater.from(context);
    }

    protected void calcHeight(){
        int line = data.size()/2;
        line += data.size()%2>0?1:0;
        int gridViewHeight= line * itemHeight;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)gridView.getLayoutParams();
        layoutParams.height = gridViewHeight;
        gridView.setLayoutParams(layoutParams);
        gridView.requestLayout();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.layout_filter_item,null);
            if( itemHeight ==0 ){
                convertView.measure(0,0);
                itemHeight = convertView.getMeasuredHeight();
                calcHeight();
            }
        }
        CheckBox ckb = ViewHolderUtil.get(convertView, R.id.layout_filter_item_name);
        ckb.setText(data.get(position).getCatName());
        //ckb.setOnCheckedChangeListener(this);
        ckb.setChecked( data.get(position).isChecked() );
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int h = buttonView.getHeight();
        int h2 =((View)buttonView.getParent()).getHeight();
        //Toast.makeText(context,"h="+h+",,,h2="+h2,Toast.LENGTH_LONG).show();
    }
}
