package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.R.string.required_data;
import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
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
import androidx.room.Room;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class RegisterProductActivity extends AppCompatActivity {

    private Product product;
    private ImageView imageView;
    private static final int SELECT_PICTURE = 100;
    private EditText etName;
    private EditText etType;
    private EditText etPrice;
    private EditText etOrigin;
    private CheckBox cbStock;
    private AppDatabase database;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);

        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etPrice = findViewById(R.id.etPrice);
        etOrigin = findViewById(R.id.etOrigin);
        cbStock = findViewById(R.id.cbStock);
        imageView = findViewById(R.id.photoContact);

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

        if (image == null) {
            Snackbar.make(this.getCurrentFocus(), R.string.select_a_profile_image, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        product = new Product(name, type, price, origin, inStock, image);


        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_PRODUCTS)
                .allowMainThreadQueries().build();
        try {
            database.productDao().insert(product);

            Toast.makeText(this, R.string.product_registered, Toast.LENGTH_LONG).show();
            etName.setText("");
            etType.setText("");
            etPrice.setText("");
            etOrigin.setText("");
            onBackPressed();

        } catch (SQLiteConstraintException sce){
            Snackbar.make(etName, R.string.error_registering, BaseTransientBottomBar.LENGTH_LONG).show();
        }
        database.close();
    }

    public void cancelButton(View view){
        onBackPressed();
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
    private byte[] uriToByteArray(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Picasso.get()
                    .load(filePath)
                    .into(imageView);
            image = uriToByteArray(filePath);
        }
    }


}


