package com.example.valkyrie.streetlightsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SupportActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ControlFragments extends Fragment implements OnMapReadyCallback{

    GoogleMap mgoogleMap;
    ToggleButton simpleToogleButton,gMapToggle;
    SupportMapFragment mapFragments;
    CountriesDbAdapter dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.control_fragment, container, false);
        dbHelper=new CountriesDbAdapter(getActivity());
        dbHelper.open();
        simpleToogleButton = (ToggleButton) v.findViewById(R.id.toogleButton1);
        simpleToogleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //GetGPS();
                    final Calendar mcurrentTime =Calendar.getInstance();
                    int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int min = mcurrentTime.get(Calendar.MINUTE);
                    final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                    final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());
                    String newEntry= "Lights is ON";
                    toogleLED("on");
                    dbHelper.createCountry(newEntry,newTime,dates,"");
                } else {
                    final Calendar mcurrentTime =Calendar.getInstance();
                    int hour =mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int min = mcurrentTime.get(Calendar.MINUTE);
                    final String dates = new SimpleDateFormat("MM/dd/yyyy").format(mcurrentTime.getInstance().getTime());
                    final String newTime = new SimpleDateFormat("hh:mm a").format(mcurrentTime.getInstance().getTime());
                    String newEntry= "Lights is OFF";
                    toogleLED("off");
                    dbHelper.createCountry(newEntry,newTime,dates,"");
                }
            }
        });
        return v;
    }
    //public void AddNew(String newEntry, String newDates,String newTime,String wala){
    //    boolean insertData = dbHelper.createCountry(newEntry,newDates,newTime,wala);
    //    if(insertData==true){

     //   }
      //  else{

      //  }

   // }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mapFragments=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment);
         mapFragments.getMapAsync(this);
    }

    public void jsonParser(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            String lat = jsonObject.getString("lat");
            String lng = jsonObject.getString("lng");
            jsonObject.getString("lng");

            Toast.makeText(getContext(),"Latitude: " + lat + "\n" + "Longitude: " + lng ,Toast.LENGTH_LONG).show();

        }catch (Exception e){

        }
    }

    public void toogleLED(String s) {

        new HttpRequestTask(
                new HttpRequest("http://192.168.43.23/Seminar/led.php?data=" + s, HttpRequest.GET),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                        ab.setTitle("Connection Timeout");
                        ab.setMessage("The server is unreachable !");
                        ab.setCancelable(true);

                        AlertDialog ad = ab.create();
                        ad.show();

                        if (response.code == 200) {
                            ad.cancel();
                        } else {
                            Toast.makeText(getContext(),"Can't connect right now",Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    public void GetGPS() {

        new HttpRequestTask(
                new HttpRequest("http://192.168.43.23/Seminar/sampledata.json" , HttpRequest.GET),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                        ab.setTitle("Connection Timeout");
                        ab.setMessage("Retrieving data from GPS...");
                        ab.setCancelable(true);

                        AlertDialog ad = ab.create();
                        ad.show();

                        if (response.code == 200) {
                            ad.cancel();
                            jsonParser(response.body);

                        } else {
                            Toast.makeText(getContext(),"Can't connect right now !",Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    public void MessageBox(String message, String title, Context context) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setCancelable(false);

        AlertDialog ad = ab.create();
       // ad.cancel();
        ad.show();
    }

    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable=api.isGooglePlayServicesAvailable(getContext());
        if(isAvailable== ConnectionResult.SUCCESS){
            return true;
        }else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(getActivity(),isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(getContext(),"Cant connect to play services",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GetGPS();
        LatLng marker = new LatLng(14.6578,120.977);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,13));
        googleMap.addMarker(new MarkerOptions().title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.lightsing)).position(marker));

        mgoogleMap = googleMap;
        if(mgoogleMap!=null){
            mgoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v=getLayoutInflater().inflate(R.layout.info_window,null);
                    TextView a = (TextView)v.findViewById(R.id.status);
                    TextView b = (TextView)v.findViewById(R.id.battery);
                    final ToggleButton c=(ToggleButton)v.findViewById(R.id.toggleButton2);

                    LatLng ll =marker.getPosition();
                    a.setText("Status:ACTIVE");
                    b.setText("Battery:30%");
                    return v;
                }
            });

        }
    }
}
