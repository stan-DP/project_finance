package com.example.administrator.project_finance.fragment;

import android.app.Fragment;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.project_finance.DataAPP;
import com.example.administrator.project_finance.NumberKeybordView;
import com.example.administrator.project_finance.R;
import com.example.administrator.project_finance.Bill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */

public class add_out_fragment extends Fragment {
    private PopupWindow keybord;
    private Context mContext;
    private View view;
    private GridView gridView;
    private int currentPosition=-1;
    DataAPP mydata;
    private int[] icon = { R.drawable.logo_car,R.drawable.logo_child,R.drawable.logo_clothes,R.drawable.logo_communicate,R.drawable.logo_daily,
    R.drawable.logo_digital,R.drawable.logo_food,R.drawable.logo_fruit,R.drawable.logo_heal,R.drawable.logo_house,R.drawable.logo_pet,
    R.drawable.logo_present,R.drawable.logo_recreation,R.drawable.logo_social,R.drawable.logo_sports,R.drawable.logo_study,R.drawable.logo_travel,
    R.drawable.logo_wine,R.drawable.logo_work,R.drawable.logo_buy,R.drawable.logo_add};
    private String[] iconName = {"汽车","孩子","服饰","通讯","日用","数码","餐饮","果蔬","医疗","住房","宠物","礼物","娱乐","社交","运动","学习","旅行","烟酒","工作","购物","其它"};
    private List<Map<String, Object>> data_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_out_fragment, container, false);
        mContext = getActivity();
        gridView=(GridView)view.findViewById(R.id.out_gridview);
        mydata = (DataAPP) getActivity().getApplication();

        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.gridview_img,R.id.gridview_text};
        SimpleAdapter sim_adapter = new SimpleAdapter(mContext, data_list, R.layout.gridview_item, from, to);
        //配置适配器
        gridView.setAdapter(sim_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mydata.isOut = true;
                mydata.billDate = mydata.currentDate.getYear() + "/" + (mydata.currentDate.getMonth() + 1) + "/" + mydata.currentDate.getDay();
                int lastPosition = currentPosition;
                currentPosition = position;
                mydata.billType = iconName[position];
                if(lastPosition>-1&&lastPosition<iconName.length) {
                    parent.getChildAt(lastPosition).findViewById(R.id.gridview_img).setSelected(false);
                }
                parent.getChildAt(currentPosition).findViewById(R.id.gridview_img).setSelected(true);
                showPopupWindow(view);
            }
        });
        return view;
    }

    public List<Map<String, Object>> getData(){
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<iconName.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    private void showPopupWindow(View parent) {
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.keybord_popupwindow, null);
        mydata.keybordResult = (TextView)contentView.findViewById(R.id.keybord_result);
        mydata.keyboardRemark = (EditText)contentView.findViewById(R.id.keybord_tip);
        keybord = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        keybord.setContentView(contentView);
        keybord.setFocusable(true);
        keybord.setOutsideTouchable(true);

        keybord.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
            }
        });

        //显示PopupWindow
        keybord.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
