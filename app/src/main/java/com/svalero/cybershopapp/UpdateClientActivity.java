package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Repair;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class UpdateClientActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvNumber;
    private TextView tvDate;
    private EditText etName;
    private EditText etSurname;
    private EditText etNumber;
    private EditText etDate;
    private CheckBox cbVip;
    private AppDatabase database;
    private String originalName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        tvName = findViewById(R.id.etName);
        tvSurname = findViewById(R.id.etSurname);
        tvNumber = findViewById(R.id.etNumber);
        tvDate = findViewById(R.id.tilDate);
        cbVip = findViewById(R.id.cbVip);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateClientActivity.this, (view, year1, month1, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();

        Client client = database.clientDao().getByName(name);

        fillData(client);
        originalName = client.getName();
    }

    private void fillData(Client client) {
        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));
        tvDate.setText(String.valueOf(client.getRegister_date()));
        cbVip.setChecked(client.isVip());


    }
    public void updateButton(View view){

        String currentName = originalName;
        String newName = etName.getText().toString();
        String newSurname = etSurname.getText().toString();
        String newNumber = etNumber.getText().toString();
        String newDate = etDate.getText().toString();
        boolean status = cbVip.isChecked();

        Client currentClient = database.clientDao().getByName(currentName);

        String sqlDate = convertDateToSqlFormat(newDate);

        Date dbDate;

        if (sqlDate != null) {
           dbDate = Date.valueOf(sqlDate);
        } else {
            dbDate = currentClient.getRegister_date();
        }


        database.clientDao().updateByName(currentName, newName, newSurname, newNumber, dbDate, status);

        Client updatedClient = database.clientDao().getByName(currentName);

        if(updatedClient != null) {
        fillData(updatedClient);
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

