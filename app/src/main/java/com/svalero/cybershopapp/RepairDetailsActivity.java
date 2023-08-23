package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_REPAIRS;

import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Repair;


import java.sql.Date;
import java.util.Locale;

public class RepairDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        Intent intent = getIntent();
        String component = intent.getStringExtra("component");

        if (component == null) return;

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_REPAIRS)
                .allowMainThreadQueries().build();

        Repair repair = database.repairDao().getByComponent(component);

        fillData(repair);
    }

    private void fillData(Repair repair) {

        TextView tvComponent = findViewById(R.id.repairComponent);
        TextView tvPrice = findViewById(R.id.repairPrice);
        TextView tvShipmentAddress = findViewById(R.id.repairAddress);
        TextView tvShipmentDate = findViewById(R.id.etShipDate);
        TextView tvRepairedDate = findViewById(R.id.etRepairDate);

        tvComponent.setText(repair.getComponent());
        tvPrice.setText(repair.getPrice());
        tvShipmentAddress.setText(repair.getShippingAddress());
        Date shipmentDate = repair.getShipmentDate();
        Date repairedDate = repair.getRepairedDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if(shipmentDate != null) {
            String formattedDate = sdf.format(repair.getShipmentDate());
            tvShipmentDate.setText(formattedDate);
        } else {
            tvShipmentDate.setText(R.string.UnknownRegistering);
        }

        if(shipmentDate.equals("01/01/0001")){
            tvShipmentDate.setText(R.string.not_shipped);
        }

        if(repairedDate != null){
            String formattedDate = sdf.format(repair.getRepairedDate());
            tvRepairedDate.setText(formattedDate);
        } else {
            tvRepairedDate.setText(R.string.UnknownRegistering);
        }

        if(repairedDate.equals("01/01/0001")){
            tvRepairedDate.setText(R.string.not_repaired);
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actonbar_preferencesmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
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