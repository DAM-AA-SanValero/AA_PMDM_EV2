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
import com.svalero.cybershopapp.contract.RepairDetailsContract;
import com.svalero.cybershopapp.contract.RepairUpdateContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.presenter.ClientDetailsPresenter;
import com.svalero.cybershopapp.presenter.RepairDetailsPresenter;
import com.svalero.cybershopapp.presenter.RepairUpdatePresenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class RepairUpdateView extends AppCompatActivity implements RepairUpdateContract.View,
        RepairDetailsContract.View {

    private RepairUpdatePresenter presenter;
    private RepairDetailsPresenter presenterDetails;

    private TextView tvComponent, tvPrice, tvShipmentAddress, tvShipmentDate, tvRepairedDate;
    private EditText etComponent, etPrice, etShipmentAddress, etShipmentDate, etRepairedDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    long repairId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_update_view);

        initializeViews();

        etShipmentDate.setOnClickListener(v -> showDatePickerDialog(etShipmentDate));
        etRepairedDate.setOnClickListener(v -> showDatePickerDialog(etRepairedDate));

        presenter = new RepairUpdatePresenter(this);
        presenterDetails = new RepairDetailsPresenter(this);

        repairId = getIntent().getLongExtra("repair_id", -1);
        if (repairId == -1) return;
        presenterDetails.getRepairDetails(repairId);
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
    public void showRepairDetails(Repair repair) {
        repairId = repair.getId();
        tvComponent.setText(repair.getComponent());
        tvPrice.setText(String.valueOf(repair.getPrice()));
        tvShipmentAddress.setText(repair.getShippingAddress());

        if (repair.getShipmentDate() != null) {
            tvShipmentDate.setText(repair.getShipmentDate().format(formatter));
        } else {
            tvShipmentDate.setText("Not shipped");
        }

        if (repair.getRepairedDate() != null) {
            tvRepairedDate.setText(repair.getRepairedDate().format(formatter));
        } else {
            tvRepairedDate.setText("Not repaired");
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    public void updateButton(View view){
        String newComponent = etComponent.getText().toString();
        float newPrice = Float.parseFloat(etPrice.getText().toString());
        String newShipAddress = etShipmentAddress.getText().toString();
        LocalDate newShipDate = LocalDate.parse(etShipmentDate.getText().toString(), formatter);
        LocalDate newRepairedDate = LocalDate.parse(etRepairedDate.getText().toString(), formatter);


        presenter.updateRepair(repairId, new Repair(newComponent, newPrice, newShipAddress,
                newShipDate, newRepairedDate));

        onBackPressed();
    }

    @Override
    public void showRepairUpdatedMessage(Repair repair) {
        showRepairDetails(repair);
        showSuccessMessage(getString(R.string.repair_updated));
    }
    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
    public void cancelButton(View view){onBackPressed();}

    private void showDatePickerDialog(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RepairUpdateView.this,
                (view, year1, month1, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year1, month1 + 1, dayOfMonth);
                    targetEditText.setText(selectedDate.format(formatter));
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
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
        String[] languages = {getString(R.string.Spanish), getString(R.string.English)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectLanguage);
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

