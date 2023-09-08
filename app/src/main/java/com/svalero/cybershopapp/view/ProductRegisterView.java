package com.svalero.cybershopapp.view;

import static com.svalero.cybershopapp.R.string.required_data;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ProductRegisterContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.presenter.ProductRegisterPresenter;

import java.util.Locale;

public class ProductRegisterView extends AppCompatActivity implements ProductRegisterContract.View {

    private ProductRegisterPresenter presenter;
    private Product product;
    private ImageView imageView;
    private static final int SELECT_PICTURE = 100;
    private EditText etName;
    private EditText etType;
    private EditText etPrice;
    private EditText etOrigin;
    private CheckBox cbStock;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_register_view);

        presenter = new ProductRegisterPresenter(this);

        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etPrice = findViewById(R.id.etPrice);
        etOrigin = findViewById(R.id.etOrigin);
        cbStock = findViewById(R.id.cbStock);
        imageView = findViewById(R.id.clientPhoto);

        imageView.setOnClickListener(v -> openGallery());
    }

    public void addButton(View view) {

        String name = etName.getText().toString();
        String type = etType.getText().toString();
        String price = etPrice.getText().toString();
        String origin = etOrigin.getText().toString();
        boolean inStock = cbStock.isChecked();

        if (name.isEmpty() || type.isEmpty() || price.isEmpty()){
            Snackbar.make(this.getCurrentFocus(), required_data, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        product = new Product(name, type, price, origin, inStock, image);
        presenter.registerProduct(product);
        onBackPressed();
    }

    public void cancelButton(View view){
        onBackPressed();
    }
    @Override
    public void showError(String errorMessage) {
        Snackbar.make(etName, errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, R.string.product_registered, Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetForm() {
        etName.setText("");
        etType.setText("");
        etPrice.setText("");
        etOrigin.setText("");
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_profile_image)), SELECT_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .into(imageView);
            image = filePath.toString();
        }
    }




}


