package com.example.valkyrie.streetlightsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //private SessionRoad session;
    //Context cntx;

    EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username=(EditText)findViewById(R.id.editText1);
        password=(EditText)findViewById(R.id.editText2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //session = new SessionRoad(cntx); //in oncreate
//and now we set sharedpreference then use this like

        //session.setusername("admin");
        //session.setpassword("admin");

    }

    @Override
    public void onBackPressed() {
        if(username.getText().toString().equals("admin")&&password.getText().toString().equals("admin")){

        }
        else{
            finish();
        }
    }

    public void loginFunction(View v){
        if(username.getText().toString().equals("admin")&&password.getText().toString().equals("admin")) {

            Toast.makeText(getApplicationContext(),"You are Logged In",Toast.LENGTH_SHORT).show();
            username.setText("");
            password.setText("");
            username.requestFocus();

            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
        }
    }
}
