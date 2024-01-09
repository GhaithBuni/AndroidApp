package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.databinding.MapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * mapActivity har som jobb att visa vägen som man sprang på en karta
 */
public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapBinding binding;
    private ArrayList<LatLng> arrayLatLng = new ArrayList<>();


    /**
     * Den börjar med att fånga datan som MainActivity skickade över och göra om den tiil double så den kan sättas in i
     * arrayLatLng så den kan senare använda positions datan
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ArrayList<String> arrayLat = getIntent()
                .getStringArrayListExtra("latitude");
        ArrayList<String> arrayLong = getIntent()
                .getStringArrayListExtra("longitude");

        for (int i = 0; i < arrayLong.size(); i++) {
            double latitude = Double.parseDouble(arrayLat.get(i));
            double longitude = Double.parseDouble(arrayLong.get(i));
            arrayLatLng.add(new LatLng(latitude, longitude));
        }
    }


    /**
     * Skapar en karta.
     * Använder polylineMaker för att för att sätta ut vägen användaren sprang på kartan
     * Sist så gör den så att man kan zooma in och ut på kartan
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        polylineMaker();
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


    /**
     * Använder arrayLatLng för att göra en polyline som vissar vägen användaren sprang.
     * Och markerar där man började springa och där man slutade.
     */
    private void polylineMaker() {

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(10);
        polylineOptions.color(ContextCompat.getColor(mapActivity.this, R.color.purple_500));
        polylineOptions.geodesic(true);
        polylineOptions.addAll(arrayLatLng);
        mMap.addPolyline(polylineOptions);
        mMap.addMarker(new MarkerOptions().position(arrayLatLng.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(arrayLatLng.get(arrayLatLng.size() - 1)).title("Finish"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayLatLng.get(0), 10));

    }
}
