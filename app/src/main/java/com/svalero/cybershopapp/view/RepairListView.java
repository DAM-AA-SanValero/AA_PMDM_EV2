package com.svalero.cybershopapp.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.RepairAdapter;
import com.svalero.cybershopapp.contract.RepairListContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.presenter.RepairListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RepairListView extends AppCompatActivity implements RepairListContract.View {

    private RepairListPresenter presenter;
    public List<Repair> repairList;
    public RepairAdapter repairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_list_view);

        presenter = new RepairListPresenter(this);
        repairList = new ArrayList<>();
        InitializeRecyclerView();
    }

    private void InitializeRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.repairList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        repairAdapter = new RepairAdapter(repairList, this);
        recyclerView.setAdapter(repairAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadAllRepairs();
    }

    @Override
    public void showRepairs(List<Repair> repairs) {
        Log.d("UI_UPDATE", "Actualizando UI con " + (repairs != null ? repairs.size() : "null") + " reparaciones.");
        repairList.clear();
        repairList.addAll(repairs);
        repairAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_addrepair_preferences, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        } else if (id == R.id.registerRepair){
            Intent intent = new Intent(this, RepairRegisterView.class);
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