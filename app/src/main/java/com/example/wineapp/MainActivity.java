package com.example.wineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    NavController navCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navCtrl = nav_host.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCtrl);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtrl.navigateUp();
                    return true;
                default:
                    return NavigationUI.onNavDestinationSelected(item, navCtrl);
            }
        }
        return true;
    }

//    private void getLastKnownLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
//                        PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                if(task.isSuccessful()){
//                    Location location=task.getResult();
//                    GeoPoint geoPoint=new GeoPoint(location.getLatitude(),location.getLongitude());
//
//                }
//            }
//        });
//    }
}