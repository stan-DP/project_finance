package com.example.administrator.project_finance.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.project_finance.DataAPP;
import com.example.administrator.project_finance.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2018/1/4.
 */

public class main_timer_fragment extends Fragment {
    private View view;
    private Context mContext;
    DataAPP mydata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_timer_fragment, container, false);
        mContext = getActivity();

        return view;
    }
}
