package com.svalero.cybershopapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

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
import com.svalero.cybershopapp.contract.ClientListContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.presenter.ClientListPresenter;

import java.util.List;

public class ClientMapView extends AppCompatActivity implements ClientListContract.View {

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;

    private ClientListPresenter clientListPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        mapView = findViewById(R.id.clientMap);
        initializePointManager();

        clientListPresenter = new ClientListPresenter(this);
        clientListPresenter.loadAllClients();
    }

    //MAP

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

    @Override
    public void showClients(List<Client> clients) {
        addClientsToMap(clients);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}