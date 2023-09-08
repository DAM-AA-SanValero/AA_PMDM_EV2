package com.svalero.cybershopapp.view;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.RepairUpdateContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.presenter.RepairUpdatePresenter;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class RepairUpdateView extends AppCompatActivity implements RepairUpdateContract.View {

    private RepairUpdatePresenter presenter;
    private TextView tvComponent, tvPrice, tvShipmentAddress, tvShipmentDate, tvRepairedDate;
    private EditText etComponent, etPrice, etShipmentAddress, etShipmentDate, etRepairedDate;
    long repairId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_update_view);

        initializeViews();

        etShipmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RepairUpdateView.this, (view, year1, month1, dayOfMonth) -> {
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(RepairUpdateView.this, (view, year1, month1, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etRepairedDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        presenter = new RepairUpdatePresenter(this);

        repairId = getIntent().getLongExtra("repair_id", -1);
        if (repairId == -1) return;
    }
    private void initializeViews() {
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

    }
    private void showRepairDetails(Repair repair) {
        tvComponent.setText(repair.getComponent());
        tvPrice.setText(repair.getPrice());
        tvShipmentAddress.setText(repair.getShippingAddress());
        tvShipmentDate.setText(String.valueOf(repair.getShipmentDate()));
        tvRepairedDate.setText(String.valueOf(repair.getRepairedDate()));
    }
    public void updateButton(View view){
        String newComponent = etComponent.getText().toString();
        String newPrice = etPrice.getText().toString();
        String newShipAddress = etShipmentAddress.getText().toString();
        String newShipDate = etShipmentDate.getText().toString();
        String newRepairedDate = etRepairedDate.getText().toString();

        String sqlShipDate = convertDateToSqlFormat(newShipDate);
        String sqlRepairedDate = convertDateToSqlFormat(newRepairedDate);

        Date dbShipmentDate = Date.valueOf(sqlShipDate);
        Date dbRepairedDate = Date.valueOf(sqlRepairedDate);

        presenter.updateRepair(repairId, new Repair(newComponent, newPrice, newShipAddress,
                dbShipmentDate, dbRepairedDate));

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

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRepairUpdatedMessage(Repair repair) {
        showRepairDetails(repair);
        showSuccessMessage("Reparación actualizada correctamente");
    }
}

