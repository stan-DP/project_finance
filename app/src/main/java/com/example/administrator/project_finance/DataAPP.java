package com.example.administrator.project_finance;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/12/18.
 */

public class DataAPP extends Application {
    public static Typeface TypeFangzheng;
    public CalendarDay currentDate=new CalendarDay();
    public TextView keybordResult;
    public EditText keyboardRemark;
    public String billType;
    public String billDate;
    public int count;
    public int timerCount;
    public boolean isOut; //true:out  false:in
    public boolean isTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        TypeFangzheng = Typeface.createFromAsset(getAssets(), "fonts/fangzheng.TTF");
        try
        {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, TypeFangzheng);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
