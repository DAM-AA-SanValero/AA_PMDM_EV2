package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.BitmapFactory;
import android.os.Bundle;

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

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.clientMap);
        initializePointManager();

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();
        List<Client> clients = database.clientDao().getAll();
        addClientsToMap(clients);
    }

    private void addClientsToMap(List<Client> clients) {
        for(Client client : clients){
            Point point =  Point.fromLngLat(client.getLongitude(), client.getLatitude());
            addMarker(point, client.getName());
        }
        if (!clients.isEmpty()) {
            Client lastClient = clients.get(clients.size() - 1);
            setCameraPosition(Point.fromLngLat(lastClient.getLongitude(),lastClient.getLatitude()));
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
                .pitch(45.0)
                .zoom(13.5)
                .bearing(-17.6)
                .build();
        mapView.getMapboxMap().setCamera(cameraPosition);
    }
}