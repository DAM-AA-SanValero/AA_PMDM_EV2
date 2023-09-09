package com.svalero.cybershopapp.view;

import static com.svalero.cybershopapp.R.string.client_registered;
import static com.svalero.cybershopapp.R.string.required_data;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
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

import com.bumptech.glide.Glide;
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
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientRegisterPresenter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClientRegisterView extends AppCompatActivity implements ClientRegisterContract.View {

    private Client client;
    private ClientRegisterPresenter presenter;

    private static final int SELECT_PICTURE = 100;
    private ImageView imageView;
    private EditText etName, etSurname, etNumber, etDate;
    private CheckBox cbVIP;
    private MapView clientMap;
    private ScrollView scrollView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;
    private String image;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_register_view);

        presenter = new ClientRegisterPresenter(this);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);
        cbVIP = findViewById(R.id.cbVip);
        clientMap = findViewById(R.id.clientMap);
        scrollView = findViewById(R.id.scrollView);
        imageView = findViewById(R.id.clientPhoto);
        imageView.setOnClickListener(v -> openGallery());

        etDate.setOnClickListener(V -> showDatePickerDialog());

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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                    etDate.setText(selectedDate.format(formatter));
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
        int number = Integer.parseInt(etNumber.getText().toString());
        String dateString = etDate.getText().toString();
        boolean vip = cbVIP.isChecked();
        boolean favourite = false;

        if (name.isEmpty() || surname.isEmpty() || number == 0 || dateString.isEmpty()){
            Snackbar.make(this.getCurrentFocus(), required_data, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        if (point == null) {
            Snackbar.make(this.getCurrentFocus(), R.string.select_the_correct_location, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        LocalDate date = dateString.isEmpty() ? LocalDate.of(0000, 01, 01)
                : LocalDate.parse(dateString, formatter);

        client = new Client(name, surname, number, date, vip
                , point.latitude(), point.longitude(), image, favourite);
        presenter.registerClient(client);
        onBackPressed();
    }

    @Override
    public void resetForm() {
        etName.setText("");
        etSurname.setText("");
        etNumber.setText("");
        etDate.setText("");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, client_registered, Toast.LENGTH_LONG).show();

    }
    @Override
    public void showError(String errorMessage) {
        Snackbar.make(etName, errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
    }
    public void cancelButton(View view){
        onBackPressed();
    }


    //MAP

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

    //ACTION BAR

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

    //IDIOMA

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


