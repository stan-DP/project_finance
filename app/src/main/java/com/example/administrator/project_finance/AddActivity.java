package com.example.administrator.project_finance;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.project_finance.fragment.*;
import com.example.administrator.project_finance.utils.StatusBarUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;


public class AddActivity extends Activity implements View.OnClickListener{
    private PopupWindow mPopWindow;
    private TextView title;
    private add_in_fragment in_fragment;
    private add_out_fragment out_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        SlidrConfig config=new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.35f)
                .build();
        Slidr.attach(this,config);

        StatusBarUtils statusBarUtils=new StatusBarUtils();
        statusBarUtils.setWindowStatusBarColor(this,R.color.wakatake);

        title=(TextView)findViewById(R.id.add_text_title);
        title.setOnClickListener(this);

        Intent intent = getIntent();
        String value = intent.getStringExtra("type");
        int position=0;
        if(value!=null)
            position=Integer.valueOf(value);

        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        switch (position){
            case 0:
                out_fragment = new add_out_fragment();
                transaction.add(R.id.add_fragment, out_fragment);
                title.setText("支出▼");
                break;
            case 1:
                in_fragment=new add_in_fragment();
                transaction.add(R.id.add_fragment, in_fragment);
                title.setText("收入▼");
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        int id = v.getId();
        switch (id){
            case R.id.add_text_title:
                showPopupWindow();
                break;
            case R.id.pop_out:
                title.setText("支出▼");
                hideAllFragment(transaction);
                if (out_fragment == null) {
                    out_fragment = new add_out_fragment();
                    transaction.add(R.id.add_fragment, out_fragment);
                } else {
                    transaction.show(out_fragment);
                }
                transaction.commit();
                mPopWindow.dismiss();
                break;
            case R.id.pop_in:
                title.setText("收入▼");
                hideAllFragment(transaction);
                if (in_fragment == null) {
                    in_fragment = new add_in_fragment();
                    transaction.add(R.id.add_fragment, in_fragment);
                } else {
                    transaction.show(in_fragment);
                }
                transaction.commit();
                mPopWindow.dismiss();
                break;
        }
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(AddActivity.this).inflate(R.layout.add_popupwindow, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new PaintDrawable());
        //设置各个控件的点击响应
        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_out);
        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_in);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        //显示PopupWindow
        mPopWindow.showAsDropDown(title);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(out_fragment!=null){
            transaction.hide(out_fragment);
        }
        if(in_fragment!=null){
            transaction.hide(in_fragment);
        }
    }

}