package com.example.administrator.project_finance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/24.
 */

public class NumberKeybordView extends KeyboardView implements
        android.inputmethodservice.KeyboardView.OnKeyboardActionListener {
    // 用于区分左下角空白的按键
    private static final int KEYCODE_EMPTY = -10;
    private int mDeleteBackgroundColor;
    private Rect mDeleteDrawRect;
    private Drawable mDeleteDrawable;
    private Context mContext;
    DataAPP mydata;

    public IOnKeyboardListener mOnKeyboardListener;

    public NumberKeybordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumberKeybordView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs,
                      int defStyleAttr) {
        mContext=context;
        mydata=(DataAPP) context.getApplicationContext();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberKeybordView, defStyleAttr, 0);
        mDeleteDrawable = a.getDrawable(
                R.styleable.NumberKeybordView_pkvDeleteDrawable);
        mDeleteBackgroundColor = a.getColor(
                R.styleable.NumberKeybordView_pkvDeleteBackgroundColor,
                Color.TRANSPARENT);

        a.recycle();

        // 设置软键盘按键的布局
        Keyboard keyboard = new Keyboard(context,
                R.xml.keyboard_number);
        setKeyboard(keyboard);

        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 遍历所有的按键
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            // 如果是左下角空白的按键，重画按键的背景
            if (key.codes[0] == KEYCODE_EMPTY) {
                drawKeyBackground(key, canvas, mDeleteBackgroundColor);
            }
            // 如果是右下角的删除按键，重画背景，并且绘制删除的图标
            else if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                drawKeyBackground(key, canvas, mDeleteBackgroundColor);
                drawDeleteButton(key, canvas);
            }
        }
    }

    // 绘制按键的背景
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas,
                                   int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        drawable.setBounds(key.x, key.y,
                key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    // 绘制删除按键
    private void drawDeleteButton(Keyboard.Key key, Canvas canvas) {
        if (mDeleteDrawable == null)
            return;

        // 计算删除图标绘制的坐标
        if (mDeleteDrawRect == null || mDeleteDrawRect.isEmpty()) {
            int intrinsicWidth = mDeleteDrawable.getIntrinsicWidth();
            int intrinsicHeight = mDeleteDrawable.getIntrinsicHeight();
            int drawWidth = intrinsicWidth;
            int drawHeight = intrinsicHeight;

            // 限制图标的大小，防止图标超出按键
            if (drawWidth > key.width) {
                drawWidth = key.width;
                drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
            }
            if (drawHeight > key.height) {
                drawHeight = key.height;
                drawWidth = drawHeight * intrinsicWidth / intrinsicHeight;
            }

            // 获取删除图标绘制的坐标
            int left = key.x + (key.width - drawWidth) / 2;
            int top = key.y + (key.height - drawHeight) / 2;
            mDeleteDrawRect = new Rect(left, top,
                    left + drawWidth, top + drawHeight);
        }

        // 绘制删除的图标
        if (mDeleteDrawRect != null && !mDeleteDrawRect.isEmpty()) {
            mDeleteDrawable.setBounds(mDeleteDrawRect.left,
                    mDeleteDrawRect.top, mDeleteDrawRect.right,
                    mDeleteDrawRect.bottom);
            mDeleteDrawable.draw(canvas);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        String result=mydata.keybordResult.getText().toString();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                if(result.length()>0)
                    result=result.substring(0,result.length()-1);
                mydata.keybordResult.setText(result);
                break;
            case Keyboard.KEYCODE_SHIFT:
                break;
            case Keyboard.KEYCODE_CANCEL: // Done
                mydata.keybordResult.setText(result);

                String fileName = ++mydata.count + ".txt";
                Bill thisBill = new Bill();
                thisBill.billType = mydata.billType;
                thisBill.remark = mydata.keyboardRemark.getText().toString();
                thisBill.money = mydata.keybordResult.getText().toString();
                thisBill.billDate = mydata.billDate;

                try {
                    FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                    if (mydata.isOut) {
                        if (thisBill.remark.equals(""))
                            thisBill.remark = " ";
                        osw.write(thisBill.billType + "_-" + thisBill.money + "_" + thisBill.remark + "_" + thisBill.billDate);
                    }
                    else {
                        if (thisBill.remark.equals(""))
                            thisBill.remark = " ";
                        osw.write(thisBill.billType + "_+" + thisBill.money + "_" + thisBill.remark + "_" + thisBill.billDate);
                    }
                    osw.flush();
                    fos.flush();
                    osw.close();
                    fos.close();
                    fileName = "count.txt";
                    FileOutputStream fos1 = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                    OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
                    String countStr = mydata.count + "";
                    osw1.write(countStr);
                    osw1.flush();
                    fos1.flush();
                    osw1.close();
                    fos1.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                this.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                break;
            case -10:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mydata.billDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                        Toast.makeText(mContext, mydata.billDate, Toast.LENGTH_SHORT).show();
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(true);
                datePicker.setCanceledOnTouchOutside(true);
                datePicker.show();
                break;
            default:
                char code = (char)primaryCode;
                result+=code;
                mydata.keybordResult.setText(result);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * 设置键盘的监听事件。
     *
     * @param listener
     *         监听事件
     */
    public void setIOnKeyboardListener(IOnKeyboardListener listener) {
        this.mOnKeyboardListener = listener;


    }

    public interface IOnKeyboardListener {

        void onInsertKeyEvent(String text);

        void onDeleteKeyEvent();
    }
}
