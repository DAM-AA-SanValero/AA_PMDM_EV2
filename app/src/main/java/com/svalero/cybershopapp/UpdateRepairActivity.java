package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;
import static com.svalero.cybershopapp.database.Constants.DATABASE_REPAIRS;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Repair;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class UpdateRepairActivity extends AppCompatActivity {
    private TextView tvComponent;
    private TextView tvPrice;
    private TextView tvShipmentAddress;
    private TextView tvShipmentDate;
    private TextView tvRepairedDate;
    private EditText etComponent;
    private EditText etPrice;
    private EditText etShipmentAddress;
    private EditText etShipmentDate;
    private EditText etRepairedDate;

    private String originalComponent;
    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_repair);

        tvComponent = findViewById(R.id.etComponent);
        tvPrice = findViewById(R.id.etPrice);
        tvShipmentAddress = findViewById(R.id.etAddress);
        tvShipmentDate = findViewById(R.id.etShipDate);
        tvRepairedDate = findViewById(R.id.etRepairDate);

        etComponent = findViewById(R.id.etComponent);
        etPrice = findViewById(R.id.etPrice);
        etShipmentAddress = findViewById(R.id.etAddress);
        etShipmentDate = findViewById(R.id.etShipDate);
        etRepairedDate = findViewById(R.id.etRepairDate);

        etShipmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRepairActivity.this, (view, year1, month1, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etShipmentDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });
        etRepairedDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRepairActivity.this, (view, year1, month1, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etRepairedDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        Intent intent = getIntent();
        String component = intent.getStringExtra("component");
        if (component == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_REPAIRS)
                .allowMainThreadQueries().build();

        Repair repair = database.repairDao().getByComponent(component);

        fillData(repair);
        originalComponent = repair.getComponent();
    }

    private void fillData(Repair repair) {
        tvComponent.setText(repair.getComponent());
        tvPrice.setText(repair.getPrice());
        tvShipmentAddress.setText(repair.getShippingAddress());
        tvShipmentDate.setText(String.valueOf(repair.getShipmentDate()));
        tvRepairedDate.setText(String.valueOf(repair.getRepairedDate()));
    }
    public void updateButton(View view){

        String currentComponent = originalComponent;
        String newComponent = etComponent.getText().toString();
        String newPrice = etPrice.getText().toString();
        String newShipAddress = etShipmentAddress.getText().toString();
        String newShipDate = etShipmentDate.getText().toString();
        String newRepairedDate = etRepairedDate.getText().toString();

        Repair currentRepair = database.repairDao().getByComponent(currentComponent);

        String sqlShipDate = convertDateToSqlFormat(newShipDate);
        String sqlRepairedDate = convertDateToSqlFormat(newRepairedDate);

        Date dbShipmentDate = null;
        Date dbRepairedDate = null;

        if (sqlShipDate != null) {
            dbShipmentDate = Date.valueOf(sqlShipDate);
        } else {
            dbShipmentDate = currentRepair.getShipmentDate();
        }

        if (sqlRepairedDate != null) {
            dbRepairedDate = Date.valueOf(sqlRepairedDate);
        } else {
            dbRepairedDate = currentRepair.getRepairedDate();
        }

        database.repairDao().updateByComponent(currentComponent, newComponent, newPrice, newShipAddress,
                dbShipmentDate, dbRepairedDate);

        Repair updatedRepair = database.repairDao().getByComponent(currentComponent);

        if(updatedRepair != null) {
            fillData(updatedRepair);
        }
        onBackPressed();
    }
    public void cancelButton(View view){onBackPressed();}


    private String convertDateToSqlFormat(String dateInOriginalFormat) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            java.util.Date date = originalFormat.parse(dateInOriginalFormat);
            return sqlFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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

