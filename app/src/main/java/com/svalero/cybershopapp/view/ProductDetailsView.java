package com.svalero.cybershopapp.view;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ProductDetailsContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.presenter.ProductDetailsPresenter;

import java.util.Locale;

public class ProductDetailsView extends AppCompatActivity implements ProductDetailsContract.View {
    private ProductDetailsPresenter presenter;

    private ImageView imageView;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_view);

        presenter = new ProductDetailsPresenter(this);
        long productId = getIntent().getLongExtra("product_id",-1);
        if(productId != -1){
            presenter.loadProductById(productId);
        }

    }

    public void showProductDetails(Product product) {

        imageView = findViewById(R.id.productPhoto);
        TextView tvName = findViewById(R.id.name);
        TextView tvType = findViewById(R.id.type);
        TextView tvPrice = findViewById(R.id.price);
        TextView tvOrigin = findViewById(R.id.origin);
        TextView tvStock = findViewById(R.id.productStatus);

        tvName.setText(product.getName());
        tvType.setText(product.getType());
        tvPrice.setText(String.valueOf(product.getPrice()));
        tvOrigin.setText(product.getOrigin());

        boolean inStock = product.isInStock();
        tvStock.setText(inStock ? getString(R.string.isVIP) : getString(R.string.isNotVip));

        image = product.getImage();

        if(image != null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
            Log.d("ProductDetails", "Loading image: " + image);

        }
    }

    @Override
    public void showErrorMessage(String message) {

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