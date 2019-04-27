package com.myjobs.backend;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.alexandroid.shpref.ShPref;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ShPref.contains("JWT")) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email= ((EditText) findViewById(R.id.email)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        try {
            JSONObject res = Utils.signup(username,email,password);
            if (res==null) {
                Log.d("NULL", "onClick: NULL");
                return;
            }
            if (res.getBoolean("success")) {
                ShPref.put("JWT", res.getString("token"));
                startActivity(new Intent(this,MainActivity.class));
            } else {
                Toast.makeText(this, "Error Authenticating", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
