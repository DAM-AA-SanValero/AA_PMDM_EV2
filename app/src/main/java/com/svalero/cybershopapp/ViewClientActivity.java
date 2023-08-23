package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewClientActivity extends AppCompatActivity  {

    public List<Client> clientList;
    public ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        clientList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.clientList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        clientAdapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(clientAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();
        clientList.clear();
        clientList.addAll(database.clientDao().getAll());
        clientAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == R.id.getMap) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        } else if (id == R.id.registerClient){
            Intent intent = new Intent(this, RegisterClientActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageSelectionDialog() {
        String[] languages = {"Español", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select language");
        builder.setItems(languages, (dialog, which) ->{
            switch (which){
                case 0:
                    setLocale("es");
                    break;
                case 1:
                    setLocale("en");
                    break;
            }
        });
        builder.create().show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources()
                .getDisplayMetrics());
        recreate();
    }

}