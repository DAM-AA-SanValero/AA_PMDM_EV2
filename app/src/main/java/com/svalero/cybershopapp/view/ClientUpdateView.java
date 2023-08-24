package com.svalero.cybershopapp.view;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientUpdatePresenter;

import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class ClientUpdateView extends AppCompatActivity implements ClientUpdateContract.View {

    private ClientUpdatePresenter presenter;
    private TextView tvName, tvSurname, tvNumber, tvDate;
    private EditText etName, etSurname, etNumber, etDate;
    private CheckBox cbVip;
    long clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        initializeViews();

        presenter = new ClientUpdatePresenter(this);

        clientId = getIntent().getLongExtra("client_id", -1);
        if (clientId == -1) return;

        etDate.setOnClickListener(v -> showDatePickerDialog());
        findViewById(R.id.updateBtn).setOnClickListener(this::updateButton);
        findViewById(R.id.cancelBtn).setOnClickListener(this::cancelButton);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ClientUpdateView.this, (view, year1, month1, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etDate.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.etName);
        tvSurname = findViewById(R.id.etSurname);
        tvNumber = findViewById(R.id.etNumber);
        tvDate = findViewById(R.id.tilDate);
        cbVip = findViewById(R.id.cbVip);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);

    }
    public void updateButton(View view){

        byte[] image = null;
        String newName = etName.getText().toString();
        String newSurname = etSurname.getText().toString();
        String newNumber = etNumber.getText().toString();
        String newDate = etDate.getText().toString();
        boolean status = cbVip.isChecked();
        double latitude = 0.0;
        double longitude = 0.0;

        if (!newName.isEmpty() && !newSurname.isEmpty() && !newNumber.isEmpty()) {
            String sqlDateStr = convertDateToSqlFormat(newDate);
            if (sqlDateStr == null) {
                showError("Formato de fecha incorrecto");
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date updatedDate;
            try {
                updatedDate = formatter.parse(sqlDateStr);
            } catch (ParseException e) {
                showError("Error al convertir la fecha.");
                return;
            }

            // Convertir java.util.Date a java.sql.Date
            java.sql.Date sqlUpdatedDate = new java.sql.Date(updatedDate.getTime());

            presenter.updateClient(clientId, new Client(newName, newSurname, newNumber,
                    sqlUpdatedDate, status, latitude, longitude, image));
            onBackPressed();
        } else {
            showError("Completa todos los campos");
        }
    }

    public void cancelButton(View view){onBackPressed();}

    private String convertDateToSqlFormat(String dateInOriginalFormat) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = originalFormat.parse(dateInOriginalFormat);
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

    public void showClientDetails(Client client) {
        clientId = client.getId();
        etName.setText(client.getName());
        etSurname.setText(client.getSurname());
        etNumber.setText(String.valueOf(client.getNumber()));
        etDate.setText(String.valueOf(client.getRegister_date()));
        cbVip.setChecked(client.isVip());
    }
    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showClientUpdatedMessage(Client client) {
        showClientDetails(client);
        showSuccessMessage("Cliente actualizado correctamente.");
    }


}

