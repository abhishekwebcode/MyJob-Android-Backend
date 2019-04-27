package com.myjobs.backend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.alexandroid.shpref.ShPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token=ShPref.getString("JWT");
        Log.d("sdf", "onCreate: "+token);
    }

    @Override
    public void onClick(View v) {
        Log.d("Sad", "onClick: "+v.getId());
        if (v.getId()==R.id.meyers) {
            startActivity(new Intent(this,TestActivity.class).putExtra("testType","meyers"));
        } else {
            startActivity(new Intent(this,TestActivity.class).putExtra("testType","holland"));
        }
    }
}
