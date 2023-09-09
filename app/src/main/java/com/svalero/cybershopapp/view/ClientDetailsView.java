package com.svalero.cybershopapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientDetailsPresenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClientDetailsView extends AppCompatActivity implements ClientDetailsContract.View {

    private ClientDetailsPresenter presenter;

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details_view);

        presenter = new ClientDetailsPresenter(this);
        long clientId = getIntent().getLongExtra("client_id", -1);
        if (clientId != -1) {
            presenter.loadClientById(clientId);
        }

        mapView = findViewById(R.id.mapping);
    }
    @Override
    public void showClientDetails(Client client) {
        ImageView imageView = findViewById(R.id.clientPhoto);
        TextView tvName = findViewById(R.id.clientName);
        TextView tvSurname = findViewById(R.id.clientSurname);
        TextView tvNumber = findViewById(R.id.clientNumber);
        TextView tvDate = findViewById(R.id.registered);
        TextView tvStatus = findViewById(R.id.clientStatus);

        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));
        LocalDate registerDate = client.getRegisterDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());

        if(registerDate != null) {
            String formattedDate = registerDate.format(formatter);
            tvDate.setText(formattedDate);
        } else {
            tvDate.setText(R.string.UnknownRegistering);
        }

        boolean isVip = client.isVip();
        tvStatus.setText(isVip ? getString(R.string.isVIP) : getString(R.string.isNotVip));

        String image = client.getImage();

        if(image != null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.person);
        }

        initializePointManager();
        addClientToMap(client);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //MAPA
    private void addClientToMap(Client client) {
        Point clientPoint = Point.fromLngLat(client.getLongitude(), client.getLatitude());
        addMarker(clientPoint, client.getName());

        if (clientPoint != null) {
            setCameraPosition(clientPoint);
        } else {
            setCameraPosition(Point.fromLngLat(-0.8738521, 41.6396971));
        }
    }

    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    private void addMarker(Point point, String name) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withTextField(name)
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
        mapView.getMapboxMap().setCamera(cameraPosition);
    }

//ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actonbar_mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == R.id.getMap) {
            Intent intent = new Intent(this, ClientMapView.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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