package com.example.page;

//google_maps_api.xml에 23번째 줄에 false다음부분이 api입력부분 참고 -현호-
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class get_gps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String location;//위도 경도
    private String []locations;
    Double latitude,longitude;
    boolean flag = false;

    void set_locations(){
        location = getIntent().getStringExtra("location");
        locations = location.split(",");
        latitude = Double.parseDouble(locations[0]);
        longitude = Double.parseDouble(locations[1]);
        flag = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gps);
        set_locations();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(flag){
            Log.w("locations",locations[0]);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);}
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng kid_location = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(kid_location).title("자녀 위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kid_location,14));
    }
}