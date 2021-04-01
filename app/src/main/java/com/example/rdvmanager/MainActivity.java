package com.example.rdvmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper myHelper;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        myHelper=new DatabaseHelper(this);
        myHelper.open();
        String[] myStringArray = getResources().getStringArray(R.array.rdv_list);
        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myStringArray);
        myListView =(ListView)findViewById(R.id.myListView);
        myListView.setAdapter(adapter);


        chargeData();
        registerForContextMenu(myListView);

        Button add_button = (Button) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeView(v);
            }
        });
    }
    public void chargeData(){
        final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE,
                DatabaseHelper.DATE, DatabaseHelper.TIME, DatabaseHelper.CONTACT, String.valueOf(DatabaseHelper.STATE)};
        final int[]to= new int[]{R.id.etID,R.id.title,R.id.etDate,R.id.etTime,R.id.contact, R.id.state};

        Cursor c = myHelper.getAllRDV();
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.rdv_item_list,c,from,to,0);
        adapter.notifyDataSetChanged();
        myListView.setAdapter(adapter);
    }

    public void changeView(View view){
        Intent intent = new Intent(this, Ajouter.class);
        startActivity(intent);
    }
}