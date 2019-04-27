package com.myjobs.backend;


import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import net.alexandroid.shpref.ShPref;

public class application extends MultiDexApplication {

        @Override
        public void onCreate() {
            super.onCreate();
            ShPref.init(this, ShPref.APPLY);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

}
