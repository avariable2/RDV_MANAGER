package com.example.rdvmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Parameter;
import java.util.Locale;

public class Parametres extends Activity
{
    Button btnFr;
    Button btnEn;

    Button btnMain;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.parametres);

        btnFr=(Button) findViewById(R.id.btnFr);
        btnFr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setAppLocale("fr");
                setContentView(R.layout.parametres);
            }
        });

        btnEn=(Button) findViewById(R.id.btnEn);
        btnEn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setAppLocale("en");
                setContentView(R.layout.parametres);
            }
        });

        btnMain=(Button) findViewById(R.id.btnMain);
        btnMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickMain(v);
            }
        });

    }
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    public void onClickNormalTheme(View view){
        Utils.changeToTheme(this, Utils.LightTheme);
        Intent intent=new Intent(this,Parametres.class);
        startActivity(intent);
    }

    public void onClickDarkTheme(View view){
        Utils.changeToTheme(this, Utils.DarkTheme);
        Intent intent2=new Intent(this,Parametres.class);
        startActivity(intent2);
    }

    public void onClickMain(View view){
        Intent intent3=new Intent(this,MainActivity.class);
        startActivity(intent3);
    }

}
