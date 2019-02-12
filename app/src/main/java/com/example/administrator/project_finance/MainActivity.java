package com.example.administrator.project_finance;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.project_finance.utils.*;
import com.example.administrator.project_finance.fragment.*;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView record_bar;
    private TextView statistics_bar;
    private TextView timer_bar;
    private main_record_fragment record_fragment;
    private main_statistics_fragment statistics_fragment;
    private main_timer_fragment timer_fragment;
    DataAPP mydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils statusBarUtils=new StatusBarUtils();
        statusBarUtils.setWindowStatusBarColor(this,R.color.wakatake);

        mydata = (DataAPP)this.getApplication();

        //initialize the mydata.count to 0
        /*
        try {
            String fileName = "count.txt";
            FileOutputStream fos1 = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
            String countStr = "0";
            osw1.write(countStr);
            osw1.flush();
            fos1.flush();
            osw1.close();
            fos1.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        */
        try {
            String fileName = "count.txt";
            FileInputStream fis = this.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String countStr = sb.toString();
            mydata.count = Integer.valueOf(countStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        FilterMenuLayout layout = (FilterMenuLayout) findViewById(R.id.filter_menu);
        FilterMenu menu = new FilterMenu.Builder(this)
                .addItem(R.drawable.out)
                .addItem(R.drawable.in)
        //.inflate(R.menu....)//inflate  menu resource
                .attach(layout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        intent.putExtra("type",Integer.toString(position));
                        startActivity(intent);
                    }
                    @Override
                    public void onMenuCollapse() {
                    }
                    @Override
                    public void onMenuExpand() {
                    }
                })
                .build();

        bindView();
    }

    private void bindView(){
        record_bar=(TextView)this.findViewById(R.id.main_text_record);
        statistics_bar=(TextView)this.findViewById(R.id.main_text_statistics);
        timer_bar=(TextView)this.findViewById(R.id.main_text_timer);

        record_bar.setOnClickListener(this);
        statistics_bar.setOnClickListener(this);
        timer_bar.setOnClickListener(this);

        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        record_fragment = new main_record_fragment();
        transaction.add(R.id.main_fragment, record_fragment);
        record_bar.setSelected(true);
        transaction.commit();
    }

    public void selected(){
        record_bar.setSelected(false);
        statistics_bar.setSelected(false);
        timer_bar.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(record_fragment!=null){
            transaction.hide(record_fragment);
        }
        if(statistics_fragment!=null){
            transaction.hide(statistics_fragment);
        }
        if(timer_fragment!=null){
            transaction.hide(timer_fragment);
        }
    }


    @Override
    public void onClick(View v){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()) {
            case R.id.main_text_record:
                selected();
                record_bar.setSelected(true);
                if (record_fragment == null) {
                    record_fragment = new main_record_fragment();
                    transaction.add(R.id.main_fragment, record_fragment);
                } else {
                    transaction.show(record_fragment);
                }
                transaction.commit();
                break;
            case R.id.main_text_statistics:
                selected();
                statistics_bar.setSelected(true);
                if (statistics_fragment == null) {
                    statistics_fragment = new main_statistics_fragment();
                    transaction.add(R.id.main_fragment, statistics_fragment);
                } else {
                    transaction.show(statistics_fragment);
                }
                transaction.commit();
                break;
            case R.id.main_text_timer:
                selected();
                timer_bar.setSelected(true);
                if (timer_fragment == null) {
                    timer_fragment = new main_timer_fragment();
                    transaction.add(R.id.main_fragment, timer_fragment);
                } else {
                    transaction.show(timer_fragment);
                }
                transaction.commit();
                break;
        }
    }


}
