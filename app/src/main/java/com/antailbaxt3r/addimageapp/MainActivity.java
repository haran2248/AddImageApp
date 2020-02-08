package com.antailbaxt3r.addimageapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.antailbaxt3r.addimageapp.R;
import com.squareup.picasso.Picasso;

import java.net.URI;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    LinearLayout openGalleryButton, openCameraButton;
    ImageView image;
    private static final int PERMISSION_REQUEST_CODE = 777;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 778;
    private static final int GALLERY_REQUEST_CODE = 107;
    private static final int CAMERA_REQUEST = 108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachID();

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EasyPermissions.hasPermissions(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    openGallery();
                }else{
                    EasyPermissions.requestPermissions(MainActivity.this,
                            "Allow this app to access your storage",
                            PERMISSION_REQUEST_CODE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

            }
        });

        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EasyPermissions.hasPermissions(MainActivity.this,
                        Manifest.permission.CAMERA)){
                    openCamera();
                }else{
                    EasyPermissions.requestPermissions(MainActivity.this,
                            "ALLOW CAMERA ACCESS",
                            CAMERA_PERMISSION_REQUEST_CODE,
                            Manifest.permission.CAMERA);
                }
            }
        });

    }

    private void openCamera() {
        Log.i("FUNCTION", "openCamera() started");
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Log.i("FUNCTION", "openGallery() started");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void attachID() {
        image = findViewById(R.id.image);
        openGalleryButton = findViewById(R.id.open_gallery);
        openCameraButton = findViewById(R.id.open_camera);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null){
            Picasso.get().load(data.getData()).into(image);
        }

        if(requestCode == CAMERA_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getExtras() != null){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }
    }
}