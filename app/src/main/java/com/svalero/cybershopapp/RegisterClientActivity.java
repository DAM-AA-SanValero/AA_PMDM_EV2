package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.R.string.client_registered;
import static com.svalero.cybershopapp.R.string.required_data;
import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.squareup.picasso.Picasso;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterClientActivity extends AppCompatActivity {

    private Client client;
    private ImageView imageView;
    private static final int SELECT_PICTURE = 100;
    private EditText etName;
    private EditText etSurname;
    private EditText etNumber;
    private EditText etDate;
    private CheckBox cbVIP;
    private MapView clientMap;
    private ScrollView scrollView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;
    private AppDatabase database;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);
        cbVIP = findViewById(R.id.cbVip);
        clientMap = findViewById(R.id.clientMap);
        scrollView = findViewById(R.id.scrollView);
        imageView = findViewById(R.id.photoContact);

        imageView.setOnClickListener(v -> openGallery());


        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(clientMap);
        gesturesPlugin.addOnMapClickListener(point -> {
            removeAllMarkers();
            this.point = point;
            addMarker(point);
            return true ;
        });
        gesturesPlugin.setPinchToZoomEnabled(true);
        gesturesPlugin.addOnMoveListener(new OnMoveListener() {
            @Override
            public void onMoveBegin(MoveGestureDetector detector) {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
            @Override
            public boolean onMove(@NonNull MoveGestureDetector detector) {
                return false;
            }
            @Override
            public void onMoveEnd(MoveGestureDetector detector) {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });
        initializePointManager();

        etDate.setOnClickListener(V -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etDate.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }



    public void addButton(View view) {

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String number = etNumber.getText().toString();
        String date = etDate.getText().toString();
        boolean vip = cbVIP.isChecked();

        if (name.isEmpty() || surname.isEmpty() || number.isEmpty() || date.isEmpty()){
            Snackbar.make(this.getCurrentFocus(), required_data, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        if (point == null) {
            Snackbar.make(this.getCurrentFocus(), R.string.select_the_correct_location, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }




        client = new Client(name, surname, number, Date.valueOf(date), vip, point.latitude(), point.longitude(), image);



        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();
        try {
            database.clientDao().insert(client);

            Toast.makeText(this, client_registered, Toast.LENGTH_LONG).show();
            etName.setText("");
            etSurname.setText("");
            etNumber.setText("");
            etDate.setText("");
            onBackPressed();

        } catch (SQLiteConstraintException sce){
            Snackbar.make(etName, R.string.error_registering, BaseTransientBottomBar.LENGTH_LONG).show();
        }
        database.close();
    }

    public void cancelButton(View view){
        onBackPressed();
    }

    private void addMarker(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.purple_marker_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(clientMap);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    private void addClientsToMap(List<Client> clients) {
        for(Client client : clients){
            Point point =  Point.fromLngLat(client.getLongitude(), client.getLatitude());
            addMarker(point);
        }

        Client lastClient = clients.get(clients.size() - 1);
        setCameraPosition(Point.fromLngLat(lastClient.getLongitude(),lastClient.getLatitude()));
    }
    private void setCameraPosition(Point point) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(point)
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        clientMap.getMapboxMap().setCamera(cameraPosition);
    }

    private void removeAllMarkers(){
        pointAnnotationManager.deleteAll();
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


