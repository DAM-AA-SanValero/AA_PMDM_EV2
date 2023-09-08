package com.svalero.cybershopapp.view;

import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.RepairDetailsContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.presenter.RepairDetailsPresenter;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RepairDetailsView extends AppCompatActivity implements RepairDetailsContract.View {

    private RepairDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_details_view);

        presenter = new RepairDetailsPresenter(this);
        long repairId = getIntent().getLongExtra("repair_id", -1);
        if (repairId != -1) {
            presenter.loadRepairById(repairId);
        }
    }
    @Override
    public void showRepairDetails(Repair repair) {

        TextView tvComponent = findViewById(R.id.repairComponent);
        TextView tvPrice = findViewById(R.id.repairPrice);
        TextView tvShipmentAddress = findViewById(R.id.repairAddress);
        TextView tvShipmentDate = findViewById(R.id.etShipDate);
        TextView tvRepairedDate = findViewById(R.id.etRepairDate);

        tvComponent.setText(repair.getComponent());
        tvPrice.setText(repair.getPrice());
        tvShipmentAddress.setText(repair.getShippingAddress());
        LocalDate shipmentDate = repair.getShipmentDate();
        LocalDate repairedDate = repair.getRepairedDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (shipmentDate != null) {
            String formattedDate = shipmentDate.format(formatter);
            tvShipmentDate.setText(formattedDate);

            if (formattedDate.equals("01/01/0001")) {
                tvShipmentDate.setText(R.string.not_shipped);
            }
        } else {
            tvShipmentDate.setText(R.string.UnknownRegistering);
        }

        if (repairedDate != null) {
            String formattedDate = repairedDate.format(formatter);
            tvRepairedDate.setText(formattedDate);

            if (formattedDate.equals("01/01/0001")) {
                tvRepairedDate.setText(R.string.not_repaired);
            }
        } else {
            tvRepairedDate.setText(R.string.UnknownRegistering);
        }
    }

    @Override
    public void showMessage(String message) {

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