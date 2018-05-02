package com.example.jiashuai.myapplicationtestapt;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.injectapi.ViewInjector;
import com.example.myannotationlib.*;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Find(R.id.tv)
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjector.injectView(this);
        tv.setTextColor(Color.parseColor("#a21111"));
        tv.setText("hahahaha");
    }
}
