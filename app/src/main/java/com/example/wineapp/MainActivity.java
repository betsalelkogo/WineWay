package com.example.wineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ImageView wineryPic;
    public Uri imageUri;
//    private FirebaseStorage storage;
//    private StorageReference storageReference;
    NavController navCtrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navCtrl = nav_host.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navCtrl);
//        wineryPic = findViewById(R.id.wineryPicture);
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

        //
        //wineryPic.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        choosePicture();
        //    }
        //});
        //
    }

//    private void choosePicture() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && requestCode == RESULT_OK && data != null && data.getData() != null){
//            imageUri = data.getData();
//            wineryPic.setImageURI(imageUri);
//            uploadPicture();
//        }
//    }
//
//    private void uploadPicture() {
//        final String randomKey = UUID.randomUUID().toString();
//        // Create a reference to "mountains.jpg"
//        StorageReference mountainsRef = storageReference.child("image/" +  randomKey);
//
//        // Create a reference to 'images/mountains.jpg'
//        StorageReference mountainImagesRef = storageReference.child("images/" + randomKey);
//
//        // While the file names are the same, the references point to different files
//        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
//        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
//    }
//
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
}