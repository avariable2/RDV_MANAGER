package com.example.rdvmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
                Intent intent=new Intent(this, Parametres.class);
                startActivity(intent);
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

        myListView = (ListView) findViewById(R.id.myListView);

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
                String state = ((TextView) v.findViewById(R.id.state)).getText().toString();
                String adresse = ((TextView) v.findViewById(R.id.etAdresse)).getText().toString();

                RDV rdv = new RDV(Long.parseLong(idItem),title,date,time,contact,Integer.parseInt(state),adresse);

                Intent intent = new Intent(getApplicationContext(), Ajouter.class);
                intent.putExtra("SelectedRDV",rdv);


                intent.putExtra("fromAdd",false);
                startActivity(intent);
            }
        });

    }
    public void chargeData(){
        final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE,
                DatabaseHelper.DATE, DatabaseHelper.TIME, DatabaseHelper.CONTACT, String.valueOf(DatabaseHelper.STATE),DatabaseHelper.ADRESSE};
        final int[]to= new int[]{R.id.idRDV,R.id.etTitle,R.id.etDate,R.id.etTime,R.id.etContact, R.id.state,R.id.etAdresse};

        Cursor c = myHelper.getAllRDV();
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.rdv_item_list,c,from,to,0);
        adapter.notifyDataSetChanged();
        myListView.setAdapter(adapter);
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