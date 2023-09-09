package com.svalero.cybershopapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.contract.ClientListContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientListView extends AppCompatActivity implements ClientListContract.View {

    public List<Client> clientList;
    public ClientAdapter clientAdapter;

    private ClientListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list_view);

        presenter = new ClientListPresenter(this);
        clientList = new ArrayList<>();
        InitializeRecyclerView();

    }

    private void InitializeRecyclerView(){
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
        presenter.loadAllClients();

    }

    @Override
    public void showClients(List<Client> clients) {
        clientList.clear();
        clientList.addAll(clients);
        clientAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        showErrorMessage(getString(R.string.error_listing_data));
    }

    //MAPA

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == R.id.getMap) {
            Intent intent = new Intent(this, ClientMapView.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        } else if (id == R.id.registerClient){
            Intent intent = new Intent(this, ClientRegisterView.class);
            startActivity(intent);
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