package com.myjobs.backend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
String testType;
TextView testype;
TextView testDesc;
TextView testTitle;
ProgressDialog dialog;
String prefix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        testType=getIntent().getStringExtra("testType");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dialog=new ProgressDialog(this);
        testTitle=((TextView)findViewById(R.id.testGreeting));
        testDesc=((TextView)findViewById(R.id.testDescription));
        testype=((TextView)findViewById(R.id.testType));
        Utils.getTest(testType,this);
    }
    public void startTest(View v) {
        startActivity(new Intent(this,TestTaker.class).putExtra("testType",testType).putExtra("prefix",prefix));
    }

    
}
