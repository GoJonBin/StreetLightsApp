package com.example.valkyrie.streetlightsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Street Lights are now On", Toast.LENGTH_LONG).show();
        SettingsFragments sf = new SettingsFragments();
        sf.toogleLED("on");
    }
}
