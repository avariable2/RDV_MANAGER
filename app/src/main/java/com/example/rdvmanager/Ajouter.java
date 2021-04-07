package com.example.rdvmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Ajouter extends AppCompatActivity {

    DatabaseHelper myHelper;

    int year, month, day;
    Button btnPickDate;
    EditText etDate;

    boolean fromAdd;

    int hours, minutes;
    Button btnPickTime;

    EditText etTime;
    EditText etTitle;
    EditText etContact;
    CheckBox state;

    EditText etAdresse;
    Button btnMaps;

    Button save;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout);

        etTitle=(EditText) findViewById(R.id.etTitle);

        etDate=(EditText) findViewById(R.id.etDate);
        btnPickDate=(Button) findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pickDate(v);
            }
        });

        etTime=(EditText) findViewById(R.id.etTime);
        btnPickTime=(Button) findViewById(R.id.btnPickTime);
        btnPickTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pickTime(v);
            }
        });

        etContact=(EditText) findViewById(R.id.etContact);
        this.state = (CheckBox) findViewById(R.id.state);

        etAdresse = (EditText) findViewById(R.id.etAdresse);
        //btnMaps=(Button) findViewById(R.id.btnMaps);
        /*btnMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                launchMaps(v);
            }
        });*/

        myHelper = new DatabaseHelper(this);
        myHelper.open();
        Intent intent = getIntent();
        fromAdd= intent.getBooleanExtra("fromAdd",true);
        if(!fromAdd){
            Bundle b= intent.getExtras();
            RDV selectedRDV= b.getParcelable("SelectedRDV");

            //tvId.setText(String.valueOf(selectedRDV.getId()));
            etTitle.setText(selectedRDV.getTitle());
            etDate.setText(selectedRDV.getDate());
            etTime.setText(selectedRDV.getTime());
            etContact.setText(selectedRDV.getContact());
            if (selectedRDV.getState()==1){
                this.state.setChecked(true);
            }
            else {
                this.state.setChecked(false);;
            }
            etAdresse.setText(selectedRDV.getAdresse());
        }

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveRDV(v);

                Intent intent=new Intent(Ajouter.this,MainActivity.class);
                startActivity(intent);
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Ajouter.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //DATE PICKER
    public void pickDate(View view){
        showDatePicker();
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            etDate.setText(new StringBuilder().append(month +1).
                    append("-").append(day).append("-").append(year).append(" "));
        }
    };
    private void showDatePicker() {
        DatePickerFragment date= new DatePickerFragment();
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Bundle args = new Bundle();
        args.putInt("year",year);
        args.putInt("month",month);
        args.putInt("day",day);
        date.setArguments(args);
        date.setCallBack(onDate);
        date.show(getSupportFragmentManager(),"Date Picker");
    }

    //TIME PICKER
    public void pickTime(View view){
        showTimePicker();
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            hours = hour;
            minutes = minute;

            etTime.setText(new StringBuilder().append(hours).append(":").append(minutes));
        }
    };

    private void showTimePicker() {
        TimePickerFragment time= new TimePickerFragment();
        final Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        Bundle args = new Bundle();
        args.putInt("hours",hours);
        args.putInt("minutes",minutes);
        time.setArguments(args);
        time.setCallBack(onTime);
        time.show(getSupportFragmentManager(),"Time Picker");
    }

    public void launchMaps(View view) {
        String map = "http://maps.google.co.in/maps?q=" + etAdresse.getText().toString() ;
        Uri gmmIntentUri = Uri.parse(map);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        /*if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }*/
    }

    public void shareMethod(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String msg = "Mon RDV est le suivant : " + etTitle.getText().toString() + " le " + etDate.getText().toString() + " à " + etTime.getText().toString() +
                " avec " + etContact.getText().toString() + " à l'adresse : " + etAdresse.getText().toString();
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        //startActivity(sendIntent);
        startActivity(Intent.createChooser(sendIntent, "Share App"));
    }

    public void saveRDV(View view) {

        String title = etTitle.getText().toString();
        String date=etDate.getText().toString();
        String time= etTime.getText().toString();
        String contact = etContact.getText().toString();
        int etState;
        if (state.isChecked()){
            etState = 1;
        }
        else{
            etState = 0;
        }
        String adresse=etAdresse.getText().toString();

        if(fromAdd) {
            RDV rdv = new RDV(title,date,time,contact,etState,adresse);
            myHelper.add(rdv);

            Intent main = new Intent(Ajouter.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
        else {
            Intent intent = getIntent();
            Bundle b= intent.getExtras();
            RDV selectedRDV= b.getParcelable("SelectedRDV");
            //etTitle.setText(selectedRDV.getTitle());
            //long id = Long.parseLong(tvId.getText().toString());
            long id = selectedRDV.getId();

            RDV rdv = new RDV(id,title,date,time,contact,etState,adresse);
            int n = myHelper.update(rdv);

            Intent main = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
    }
}
