package com.svalero.cybershopapp.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.ProductAdapter;
import com.svalero.cybershopapp.contract.ProductListContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.presenter.ProductListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListView extends AppCompatActivity implements ProductListContract.View {

    public List<Product> productList;
    public ProductAdapter productAdapter;
    private ProductListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_view);

        presenter = new ProductListPresenter(this);
        productList = new ArrayList<>();

        InitializeRecyclerView();
    }

    private void InitializeRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.productList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadAllProducts();


    }
    @Override
    public void showProducts(List<Product> products) {

        productList.clear();
        productList.addAll(products);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_addproduct_preferences, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        } else if (id == R.id.registerProduct){
            Intent intent = new Intent(this, ProductRegisterView.class);
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