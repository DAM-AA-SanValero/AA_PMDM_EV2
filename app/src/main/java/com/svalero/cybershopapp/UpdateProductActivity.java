package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;
import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;

import android.content.Intent;
import android.content.res.Configuration;
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

import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;

import java.util.Locale;

public class UpdateProductActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvType;
    private TextView tvPrice;

    private TextView tvOrigin;
    private EditText etName;
    private EditText etType;
    private EditText etPrice;
    private EditText etOrigin;
    private CheckBox cbStock;
    private AppDatabase database;
    private String originalName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        tvName = findViewById(R.id.etName);
        tvType = findViewById(R.id.etType);
        tvPrice = findViewById(R.id.etPrice);
        tvOrigin = findViewById(R.id.etOrigin);
        cbStock = findViewById(R.id.cbStock);

        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etPrice = findViewById(R.id.etPrice);
        etOrigin = findViewById(R.id.etOrigin);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_PRODUCTS)
                .allowMainThreadQueries().build();

        Product product = database.productDao().getByName(name);

        fillData(product);
        originalName = product.getName();
    }

    private void fillData(Product product) {
        tvName.setText(product.getName());
        tvType.setText(product.getType());
        tvPrice.setText(String.valueOf(product.getPrice()));
        tvOrigin.setText(product.getOrigin());
        cbStock.setChecked(product.isInStock());
    }
    public void updateButton(View view){

        String currentName = originalName;
        String newName = etName.getText().toString();
        String newType = etType.getText().toString();
        String newPrice = etPrice.getText().toString();
        String newOrigin = etOrigin.getText().toString();
        boolean isInStock = cbStock.isChecked();

        database.productDao().updateByName(currentName, newName, newType, newPrice, newOrigin, isInStock);

        Product updatedProduct = database.productDao().getByName(currentName);

        if(updatedProduct != null) {
        fillData(updatedProduct);
        }

        onBackPressed();

    }
    public void cancelButton(View view){onBackPressed();}

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

