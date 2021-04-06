package com.example.rdvmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView tvId;
    EditText etTitle;
    EditText etContact;

    CheckBox state;

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
        state = (CheckBox) findViewById(R.id.state);

        myHelper = new DatabaseHelper(this);
        myHelper.open();
        Intent intent = getIntent();
        fromAdd= intent.getBooleanExtra("fromAdd",false);
        if(!fromAdd){
            Bundle b= intent.getExtras();
            RDV selectedRDV= b.getParcelable("SelectedRDV");

            tvId.setText(String.valueOf(selectedRDV.getId()));
            etTitle.setText(selectedRDV.getTitle());
            etDate.setText(selectedRDV.getDate());
            etTime.setText(selectedRDV.getTime());
            etContact.setText(selectedRDV.getContact());
            if (selectedRDV.getState() == 0){
                state.setChecked(false);
            }
            else {
                state.setChecked(true);
            }
        }
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

    public void saveRDV(View view) {

        String title = etTitle.getText().toString();
        String date=etDate.getText().toString();
        String time= etTime.getText().toString();
        String contact = etContact.getText().toString();
        int etState;
        if (state.getText().toString().equals(0)){
            etState = 0;
        }
        else{
            etState = 1;
        }

        if(fromAdd) {
            RDV rdv = new RDV(title,date,time,contact,etState);
            myHelper.add(rdv);

            Intent main = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
        else {
            Long id = Long.parseLong(tvId.getText().toString());

            RDV rdv = new RDV(id,title,date,time,contact,Integer.parseInt(state.toString()));
            int n = myHelper.update(rdv);

            Intent main = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
    }
}
