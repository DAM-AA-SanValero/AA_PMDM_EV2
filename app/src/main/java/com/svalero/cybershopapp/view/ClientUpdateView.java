package com.svalero.cybershopapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientDetailsPresenter;
import com.svalero.cybershopapp.presenter.ClientUpdatePresenter;

import java.time.LocalDate;
import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class ClientUpdateView extends AppCompatActivity implements ClientUpdateContract.View,
        ClientDetailsContract.View {

    private ClientDetailsPresenter presenterDetails;
    private ClientUpdatePresenter presenter;
    private EditText etName, etSurname, etNumber, etDate;
    private MapView clientMap;
    private ScrollView scrollView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;


    private CheckBox cbVip;
    private Boolean favouriteValue = null;

    long clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_update_view);

        initializeViews();
        presenter = new ClientUpdatePresenter(this);
        presenterDetails = new ClientDetailsPresenter(this);

        clientId = getIntent().getLongExtra("client_id", -1);
        if (clientId == -1) return;
        presenterDetails.getClientDetails(clientId);;



        etDate.setOnClickListener(v -> showDatePickerDialog());
        findViewById(R.id.updateBtn).setOnClickListener(this::updateButton);
        findViewById(R.id.cancelBtn).setOnClickListener(this::cancelButton);
    }
    private void initializeViews() {
        scrollView = findViewById(R.id.scrollView);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);
        cbVip = findViewById(R.id.cbVip);

        clientMap = findViewById(R.id.clientMap);

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
    }

    public void showClientDetails(Client client) {
        clientId = client.getId();
        etName.setText(client.getName());
        etSurname.setText(client.getSurname());
        etNumber.setText(String.valueOf(client.getNumber()));
        etDate.setText(client.getRegister_date());
        cbVip.setChecked(client.isVip());
        favouriteValue = client.getFavourite();

        initializePointManager();
        addClientToMap(client);
    }

    @Override
    public void showMessage(String message) {

    }

    public void updateButton(View view){

        String image = null;
        String newName = etName.getText().toString();
        String newSurname = etSurname.getText().toString();
        String newNumber = etNumber.getText().toString();
        String newDate = etDate.getText().toString();
        boolean status = cbVip.isChecked();
        double latitude = (this.point != null) ? this.point.latitude() : 0.0;
        double longitude = (this.point != null) ? this.point.longitude() : 0.0;

        if (!newName.isEmpty() && !newSurname.isEmpty() && !newNumber.isEmpty()) {

            presenter.updateClient(clientId, new Client(newName, newSurname, newNumber,
                    newDate, status, latitude, longitude, image, favouriteValue ));
            onBackPressed();
        } else {
            showError("Completa todos los campos");
        }
    }

    public void cancelButton(View view){onBackPressed();}

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ClientUpdateView.this, (view, year1, month1, dayOfMonth) -> {
            LocalDate selectedDate = LocalDate.of(year1, month1 + 1, dayOfMonth);
            etDate.setText(selectedDate.toString());
        }, year, month, day);
        datePickerDialog.show();
    }
    private String convertDateToSqlFormat(String dateInOriginalFormat) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = originalFormat.parse(dateInOriginalFormat);
            return sqlFormat.format(date);
        } catch (ParseException e) {
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


    private void addClientToMap(Client client) {
        Point clientPoint = Point.fromLngLat(client.getLongitude(), client.getLatitude());
        addMarker(clientPoint);

        if (clientPoint != null) {
            setCameraPosition(clientPoint);
        } else {
            setCameraPosition(Point.fromLngLat(-0.8738521, 41.6396971));
        }
    }

    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(clientMap);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    private void addMarker(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.purple_marker_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void setCameraPosition(Point point) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(point)
                .pitch(20.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        clientMap.getMapboxMap().setCamera(cameraPosition);
    }

    private void removeAllMarkers(){
        pointAnnotationManager.deleteAll();
    }
    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showClientUpdatedMessage(Client client) {
        showClientDetails(client);
        showSuccessMessage("Cliente actualizado correctamente.");
    }


}

