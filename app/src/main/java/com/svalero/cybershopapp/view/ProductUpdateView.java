package com.svalero.cybershopapp.view;

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
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ProductDetailsContract;
import com.svalero.cybershopapp.contract.ProductUpdateContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.presenter.ProductDetailsPresenter;
import com.svalero.cybershopapp.presenter.ProductUpdatePresenter;

import java.util.Locale;

public class ProductUpdateView extends AppCompatActivity implements ProductUpdateContract.View,
        ProductDetailsContract.View {

    private static final int SELECT_PICTURE = 100;
    private ProductDetailsPresenter presenterDetails;
    private ProductUpdatePresenter presenter;
    private String image;
    private ImageView imageView;
    private EditText etName, etType, etPrice, etOrigin;
    private CheckBox cbStock;
    long productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_update_view);

        initializeViews();
        presenter = new ProductUpdatePresenter(this);
        presenterDetails = new ProductDetailsPresenter(this);

        productId = getIntent().getLongExtra("product_id", -1);
        if (productId == -1) return;
        presenterDetails.getProductDetails(productId);


    }

    private void initializeViews() {
        imageView = findViewById(R.id.productPhoto);
        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etPrice = findViewById(R.id.etPrice);
        etOrigin = findViewById(R.id.etOrigin);
        cbStock = findViewById(R.id.cbStock);
    }

    public void showProductDetails(Product product) {
        productId = product.getId();
        etName.setText(product.getName());
        etType.setText(product.getType());
        etPrice.setText(String.valueOf(product.getPrice()));
        etOrigin.setText(product.getOrigin());
        cbStock.setChecked(product.isInStock());
        image = product.getImage();

        if(image != null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        }
        imageView.setOnClickListener(v -> openGallery());

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void updateButton(View view){

        String newName = etName.getText().toString();
        String newType = etType.getText().toString();
        String newPrice = etPrice.getText().toString();
        String newOrigin = etOrigin.getText().toString();
        boolean isInStock = cbStock.isChecked();

        presenter.updateProduct(productId, new Product(newName, newType,
                newPrice, newOrigin, isInStock, image));
        onBackPressed();

    }
    public void cancelButton(View view){onBackPressed();}

    @Override
    public void showProductUpdatedMessage(Product product) {
        showProductDetails(product);
        showSuccessMessage("Producto actualizado correctamente.");
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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

    //ACTION BAR, IDIOMAS

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

