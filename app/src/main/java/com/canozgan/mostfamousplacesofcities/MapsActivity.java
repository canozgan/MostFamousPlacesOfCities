package com.canozgan.mostfamousplacesofcities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.canozgan.mostfamousplacesofcities.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationListener locationListener;
    LocationManager locationManager;
    boolean info;
    Double selectedLatitude;
    Double selectedLongitude;
    String value;
    int id;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        registerLauncher();
        info =false;
        firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        binding.saveButton.setEnabled(false);
        Intent intent=getIntent();
        value=intent.getStringExtra("value");
        selectedLatitude=intent.getDoubleExtra("latitute",0.0);
        selectedLongitude=intent.getDoubleExtra("longitute",0.0);
        if(value.matches("show")){
            binding.saveButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(!value.matches("show")){
            mMap.setOnMapLongClickListener(MapsActivity.this);
            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener=new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if(!info){
                        if(!value.matches("show")) {
                            LatLng changedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(changedLatLng, 15));
                            info = true;
                        }
                    }

                }

            };
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.getRoot(),"Permission needed for location",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }).show();

                }
                else{
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                if(!value.matches("show")){
                    Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastLocation!=null){
                        LatLng lastLatLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,15));
                    }
                }
                mMap.setMyLocationEnabled(true);
            }
        }else{
            LatLng selectedLatLng=new LatLng(selectedLatitude,selectedLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng,15));
            mMap.addMarker(new MarkerOptions().position(selectedLatLng));
        }



    }
    public void registerLauncher(){
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                        if(!value.matches("show")){
                            Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(lastLocation!=null){
                                LatLng lastLatLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,15));
                            }
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(MapsActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        selectedLatitude=latLng.latitude;
        selectedLongitude=latLng.longitude;
        binding.saveButton.setEnabled(true);
    }
    public void save(View view){
        Intent intentToUploadActivity=new Intent(this,UploadActivity.class);
        intentToUploadActivity.putExtra("latitute",selectedLatitude);
        intentToUploadActivity.putExtra("longitute",selectedLongitude);
        startActivity(intentToUploadActivity);
        finish();
    }
}