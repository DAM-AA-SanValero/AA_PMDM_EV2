package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.R.string.required_data;
import static com.svalero.cybershopapp.database.Constants.DATABASE_REPAIRS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.database.AppDatabase;

import com.svalero.cybershopapp.domain.Repair;

import java.sql.Date;
import java.util.Calendar;

import java.util.Locale;

public class RegisterRepairActivity extends AppCompatActivity {

    private Repair repair;
    private EditText etComponent;
    private EditText etPrice;
    private EditText etShipmentAddress;
    private EditText etShipmentDate;
    private EditText etRepairedDate;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_repair);

        etComponent = findViewById(R.id.etComponent);
        etPrice = findViewById(R.id.etPrice);
        etShipmentAddress = findViewById(R.id.etAddress);
        etShipmentDate = findViewById(R.id.etShipDate);
        etRepairedDate = findViewById(R.id.etRepairDate);

        etShipmentDate.setOnClickListener(V -> showDatePickerDialog_shipDate());
        etRepairedDate.setOnClickListener(V -> showDatePickerDialog_repairDate());

    }

    private void showDatePickerDialog_shipDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etShipmentDate.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showDatePickerDialog_repairDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etRepairedDate.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }



    public void addButton(View view) {

        String component = etComponent.getText().toString();
        String price = etPrice.getText().toString();
        String shipmentAddress = etShipmentAddress.getText().toString();
        String shipmentDate = etShipmentDate.getText().toString();
        String repairedDate = etRepairedDate.getText().toString();

        if (component.isEmpty() || price.isEmpty() || shipmentAddress.isEmpty()) {
            Snackbar.make(this.getCurrentFocus(), required_data, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        if (shipmentDate.isEmpty()){
            shipmentDate = "0000-01-01";
        }

        if (repairedDate.isEmpty()){
            repairedDate = "0000-01-01";
        }

        repair = new Repair(component, price, shipmentAddress, Date.valueOf(shipmentDate), Date.valueOf(repairedDate));


        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_REPAIRS)
                .allowMainThreadQueries().build();
        try {
            database.repairDao().insert(repair);

            Toast.makeText(this, R.string.repair_registered, Toast.LENGTH_LONG).show();
            etComponent.setText("");
            etPrice.setText("");
            etShipmentAddress.setText("");
            etShipmentDate.setText("");
            etRepairedDate.setText("");
            onBackPressed();

        } catch (SQLiteConstraintException sce) {
            Snackbar.make(etComponent, R.string.error_registering, BaseTransientBottomBar.LENGTH_LONG).show();
        }
        database.close();
    }

    public void cancelButton(View view) {
        onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actonbar_preferencesmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences) {
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageSelectionDialog() {
        String[] languages = {"Español", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select language");
        builder.setItems(languages, (dialog, which) -> {
            switch (which) {
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


