package com.example.rdvmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper myHelper;
    ListView myListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.rdv_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_rdv:{
                Intent intent=new Intent(this,Ajouter.class);
                startActivity(intent);
                return true;
            }
            case R.id.parametre: {
                Toast.makeText(this, "Paramètres", Toast.LENGTH_LONG).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        myHelper = new DatabaseHelper(this);
        myHelper.open();
        String[] myStringArray = getResources().getStringArray(R.array.rdv_list);
        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);
        myListView = (ListView) findViewById(R.id.myListView);
        myListView.setEmptyView(findViewById(R.id.tvEmpty));
        myListView.setAdapter(adapter);

        chargeData();
        registerForContextMenu(myListView);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                String idItem= ((TextView) v.findViewById(R.id.idRDV)).getText().toString();
                String title = ((TextView) v.findViewById(R.id.etTitle)).getText().toString();
                String date = ((TextView) v.findViewById(R.id.etDate)).getText().toString();
                String time = ((TextView) v.findViewById(R.id.etTime)).getText().toString();
                String contact = ((TextView) v.findViewById(R.id.etContact)).getText().toString();
                int state = Integer.parseInt(((TextView) v.findViewById(R.id.state)).getText().toString());

                RDV rdv = new RDV(Long.parseLong(idItem),title,date,time,contact,state);

                Intent intent = new Intent(getApplicationContext(), Ajouter.class);
                intent.putExtra("SelectedRDV",rdv);


                intent.putExtra("fromAdd",false);
                startActivity(intent);
            }
        });

    }
    public void chargeData(){
        final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE,
                DatabaseHelper.DATE, DatabaseHelper.TIME, DatabaseHelper.CONTACT, String.valueOf(DatabaseHelper.STATE)};
        final int[]to= new int[]{R.id.etTitle,R.id.etDate,R.id.etTime,R.id.etContact, R.id.state};

        Cursor c = myHelper.getAllRDV();
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.rdv_item_list,c,from,to,0);
        adapter.notifyDataSetChanged();
        myListView.setAdapter(adapter);
    }

    public void changeView(View view){
        Intent intent = new Intent(this, Ajouter.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId()==R.id.delete){
            myHelper.delete(info.id);
            chargeData();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}