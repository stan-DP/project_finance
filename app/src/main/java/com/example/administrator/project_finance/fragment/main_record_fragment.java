package com.example.administrator.project_finance.fragment;

import com.example.administrator.project_finance.*;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.project_finance.DataAPP;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */

public class main_record_fragment extends Fragment implements View.OnClickListener{
    private ListView listView;
    private Context mContext;
    private View view;
    private TextView yearView,dayView,triangleView,titleView;
    private Button calenderConfirmButton;
    DataAPP mydata;
    private MaterialCalendarView materialCalendarView;//布局内的控件
    private CalendarDay currentDate;//自定义的日期对象
    private PopupWindow mPopWindow;
    private double outMoney;
    private double inMoney;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_record_fragment, container, false);
        mContext = getActivity();
        mydata = (DataAPP) getActivity().getApplication();
        currentDate=mydata.currentDate;

        listView = (ListView)view.findViewById(R.id.main_record_list);
        List<Map<String, String>> list=getData();
        listView.setAdapter(new ListViewAdapter(mContext, list));

        yearView=(TextView)view.findViewById(R.id.record_text_year);
        dayView=(TextView)view.findViewById(R.id.record_text_day);
        triangleView=(TextView)view.findViewById(R.id.record_text_triangle);
        int year=currentDate.getYear();
        int month=currentDate.getMonth();
        int day=currentDate.getDay();
        yearView.setText(year+"年");
        dayView.setText((month+1)+"月");
        yearView.setOnClickListener(this);
        dayView.setOnClickListener(this);
        triangleView.setOnClickListener(this);

        titleView=(TextView)view.findViewById(R.id.record_text_title);
        Typeface typeFace =Typeface.createFromAsset(mContext.getAssets(),"fonts/kaishu.ttf");
        titleView.setTypeface(typeFace);

        return view;
    }

    public List<Map<String, String>> getData(){
        int recordCount = 0;
        outMoney = inMoney = 0.0;
        List<Map<String, String>> list=new ArrayList<Map<String,String>>();
        for (int i = mydata.count; i > 0; i--) {
            try {
                String fileName = i + ".txt";
                FileInputStream fis = mContext.openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String billRecord = sb.toString();
                String dateStr = currentDate.getYear() + "/" + (currentDate.getMonth() + 1) + "/" + currentDate.getDay();
                if (billRecord.split("_")[3].equals(dateStr)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("info", billRecord.split("_")[0] + ":   " + billRecord.split("_")[2] + "    " + billRecord.split("_")[1]);
                    if (billRecord.split("_")[1].substring(0, 1).equals("-")) {
                        outMoney += Double.valueOf(billRecord.split("_")[1].substring(1));
                    }
                    if (billRecord.split("_")[1].substring(0, 1).equals("+")) {
                        inMoney += Double.valueOf(billRecord.split("_")[1].substring(1));
                    }
                    list.add(map);
                    recordCount++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = recordCount + 1; i <= 10; i++) {
            Map<String, String> map=new HashMap<String, String>();
            map.put("info", "记录"+i + ": 无记录");
            list.add(map);
        }
        TextView outNumber = (TextView)view.findViewById(R.id.record_number_out);
        TextView inNumber = (TextView)view.findViewById(R.id.record_number_in);
        outNumber.setText(String.valueOf(outMoney));
        inNumber.setText(String.valueOf(inMoney));
        return list;
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;
        private List<Map<String, String>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public final class Item{
            public TextView delete;
            public TextView info;
        }
        public ListViewAdapter(Context context,List<Map<String, String>> data){
            this.context=context;
            this.data=data;
            this.layoutInflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return data.size();
        }
        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return itemViews[position];
        }
        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item=null;
            if(convertView==null){
                item=new Item();
                //获得组件，实例化组件
                convertView=layoutInflater.inflate(R.layout.main_record_item, null);
                item.info=(TextView)convertView.findViewById(R.id.main_record_info);
                item.delete=(TextView)convertView.findViewById(R.id.main_record_delete);
                convertView.setTag(item);
            }else{
                item=(Item) convertView.getTag();
            }
            //绑定数据
            item.info.setText(data.get(position).get("info"));
            return convertView;
        }
    }

    private void showPopupWindow(View parent) {
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.main_calender, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);

        materialCalendarView=(MaterialCalendarView)contentView.findViewById(R.id.calendarView);
        calenderConfirmButton=(Button)contentView.findViewById(R.id.calender_confirm);
        calenderConfirmButton.setOnClickListener(this);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2015,1,1))
                .setMaximumDate(CalendarDay.from(2020,12,30))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                currentDate = date;
                List<Map<String, String>> list=getData();
                listView.setAdapter(new ListViewAdapter(mContext, list));
            }
        });

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        //显示PopupWindow
        mPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        int id = v.getId();
        switch (id){
            case R.id.record_text_day:
                WindowManager.LayoutParams lpd = getActivity().getWindow().getAttributes();
                lpd.alpha = 0.4f;
                getActivity().getWindow().setAttributes(lpd);
                if(mPopWindow==null) {
                    showPopupWindow(view);
                }else{
                    mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.record_text_triangle:
                WindowManager.LayoutParams lpt = getActivity().getWindow().getAttributes();
                lpt.alpha = 0.4f;
                getActivity().getWindow().setAttributes(lpt);
                if(mPopWindow==null) {
                    showPopupWindow(view);
                }else{
                    mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.record_text_year:
                WindowManager.LayoutParams lpy = getActivity().getWindow().getAttributes();
                lpy.alpha = 0.4f;
                getActivity().getWindow().setAttributes(lpy);
                if(mPopWindow==null) {
                    showPopupWindow(view);
                }else{
                    mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.calender_confirm:
                int year=currentDate.getYear();
                int month=currentDate.getMonth();
                int day=currentDate.getDay();
                yearView.setText(year+"年");
                dayView.setText((month+1)+"月");
                mPopWindow.dismiss();
                break;
        }
    }

}
