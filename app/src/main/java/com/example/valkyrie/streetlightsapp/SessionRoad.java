package com.example.valkyrie.streetlightsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionRoad {
    private SharedPreferences prefs;

    public SessionRoad(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String usename) {
        prefs.edit().putString("admin", usename).commit();
    }

    public String getusername() {
        String usename = prefs.getString("admin","");
        return usename;
    }

    public void setpassword(String usename) {
        prefs.edit().putString("admin", usename).commit();
    }

    public String getpassword() {
        String usename = prefs.getString("admin","");
        return usename;
    }
}
