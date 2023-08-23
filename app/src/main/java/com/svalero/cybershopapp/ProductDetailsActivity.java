package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;
import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;

import java.sql.Date;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        if (name == null) return;

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_PRODUCTS)
                .allowMainThreadQueries().build();

        Product product = database.productDao().getByName(name);

        fillData(product);

    }

    private void fillData(Product product) {

        ImageView imageView = findViewById(R.id.productPhoto);
        TextView tvName = findViewById(R.id.name);
        TextView tvType = findViewById(R.id.type);
        TextView tvPrice = findViewById(R.id.price);
        TextView tvOrigin = findViewById(R.id.origin);
        TextView tvStock = findViewById(R.id.productStatus);

        tvName.setText(product.getName());
        tvType.setText(product.getType());
        tvPrice.setText(product.getPrice());
        tvOrigin.setText(product.getOrigin());

        boolean inStock = product.isInStock();
        tvStock.setText(inStock ? getString(R.string.isVIP) : getString(R.string.isNotVip));

        byte[] image = product.getImage();

        if (image != null && image.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.product);
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
}