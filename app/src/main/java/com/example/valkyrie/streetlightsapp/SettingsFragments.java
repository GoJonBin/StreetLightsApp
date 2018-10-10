package com.example.valkyrie.streetlightsapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SettingsFragments extends Fragment{
    TextView clickedEdit,clickedEditOff,clickedEditShow,clickedEditOffShow;
    ToggleButton tglbtn,tglbtnOff;
    PendingIntent pd,pdOff;
    AlarmManager am,amOff;
    private CountriesDbAdapter dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.settings_fragment,container,false);
        dbHelper=new CountriesDbAdapter(getActivity());
        dbHelper.open();
        am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        amOff=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        tglbtn=v.findViewById(R.id.angToggle);
        tglbtnOff=v.findViewById(R.id.angToggleOff);
        clickedEdit=v.findViewById(R.id.editTextTime);
        clickedEditShow=v.findViewById(R.id.editTextTimeShow);
        clickedEditOff=v.findViewById(R.id.editTextTimeOff);
        clickedEditOffShow=v.findViewById(R.id.editTextTimeOffShow);
        clickedEditShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime =Calendar.getInstance();
                int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int min = mcurrentTime.get(Calendar.MINUTE);
                final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hoursOfDay, int minutex) {
                        int newHours=0;
                        String am_pm="";
                        if(hoursOfDay<=12){
                            am_pm="AM";
                            newHours=hoursOfDay;
                        }
                        else if(hoursOfDay>12){
                            newHours=hoursOfDay-12;
                            am_pm="PM";
                        }
                        clickedEditShow.setText(newHours+":"+minutex+" "+am_pm);
                        clickedEdit.setText(newHours+":"+minutex+" "+am_pm+"  "+dates);
                    }
                },hour,min,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        clickedEditOffShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime =Calendar.getInstance();
                int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int min = mcurrentTime.get(Calendar.MINUTE);
                final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hoursOfDay, int minutex) {
                        int newHours=0;
                        String am_pm="";
                        if(hoursOfDay<=12){
                            am_pm="AM";
                            newHours=hoursOfDay;
                        }
                        else if(hoursOfDay>12){
                            newHours=hoursOfDay-12;
                            am_pm="PM";
                        }
                        clickedEditOffShow.setText(newHours+":"+minutex+" "+am_pm);
                        clickedEditOff.setText(newHours+":"+minutex+" "+am_pm+"  "+dates);
                    }
                },hour,min,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tglbtn.setOnClickListener(new View.OnClickListener() {
            long time;
            @Override
            public void onClick(View v) {
                if(tglbtn.isChecked()){
                    final Calendar mcurrentTime =Calendar.getInstance();
                    int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int min = mcurrentTime.get(Calendar.MINUTE);
                    final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                    final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());

                    String result = clickedEdit.getText().toString();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a  MM/dd/yyyy");
                    try{
                        String newEntry = "Lights set ON";
                        Date times = sdf.parse(result);
                        time = times.getTime();
                        Intent intent = new Intent(getContext(),Alarm.class);
                        pd =PendingIntent.getBroadcast(getContext(),0,intent,0);
                        am.set(AlarmManager.RTC_WAKEUP,time,pd);
                        Toast.makeText(getContext(),"StreetLights are set to On "+time+"  "+times+"  "+result,Toast.LENGTH_LONG).show();
                        dbHelper.createCountry(newEntry,newTime,dates,"");
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }
                }else{
                    if(am!=null){
                        final Calendar mcurrentTime =Calendar.getInstance();
                        int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int min = mcurrentTime.get(Calendar.MINUTE);
                        final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                        final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());
                        String newEntry = "Lights set ON cancel";
                        am.cancel(pd);
                        Toast.makeText(getContext(),"Cancelled",Toast.LENGTH_LONG).show();
                        dbHelper.createCountry(newEntry,newTime,dates,"");
                    }
                }
            }
        });

        tglbtnOff.setOnClickListener(new View.OnClickListener() {
            long time;
            @Override
            public void onClick(View v) {
                if(tglbtnOff.isChecked()){
                    final Calendar mcurrentTime =Calendar.getInstance();
                    int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int min = mcurrentTime.get(Calendar.MINUTE);
                    final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                    final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());
                    String result = clickedEditOff.getText().toString();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a  MM/dd/yyyy");
                    try{
                        String newEntry = "Lights set OFF";
                        Date times = sdf.parse(result);
                        time = times.getTime();
                        Intent intent = new Intent(getContext(),AlarmOff.class);
                        pdOff =PendingIntent.getBroadcast(getContext(),0,intent,0);
                        amOff.set(AlarmManager.RTC_WAKEUP,time,pdOff);
                        Toast.makeText(getContext(),"StreetLights are set to Off "+time+"  "+times+"  "+result,Toast.LENGTH_LONG).show();
                        dbHelper.createCountry(newEntry,newTime,dates,"");
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }
                }else{
                    if(amOff!=null){
                        final Calendar mcurrentTime =Calendar.getInstance();
                        int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int min = mcurrentTime.get(Calendar.MINUTE);
                        final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                        final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());
                        String newEntry = "Lights set OFF cancel";
                        amOff.cancel(pdOff);
                        Toast.makeText(getContext(),"Cancelled",Toast.LENGTH_LONG).show();
                        dbHelper.createCountry(newEntry,newTime,dates,"");
                    }
                }
            }
        });
        return v;
    }

    public void toogleLED(String s) {

        new HttpRequestTask(
                new HttpRequest("http://192.168.43.23/Seminar/led.php?data=" + s, HttpRequest.GET),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        //AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                        //ab.setTitle("Bahala ako sa title");
                        //ab.setMessage("Ano kaya");
                        //ab.setCancelable(true);

                        //AlertDialog ad = ab.create();
                        //ad.show();

                        if (response.code == 200) {
                            //ad.cancel();
                        } else {
                            //Toast.makeText(getContext(),"EDI WOW",Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }
}