package com.example.cameraapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView result;
//    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    Bitmap myBitmap;
    String encodedImg;
    Bitmap decodeImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Button btnconvert = findViewById(R.id.button2);
        Button btnDecode = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
        result = findViewById(R.id.imageView);

        btnconvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapToBase64();
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base64ToImage();
            }
        });



    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                File imgFile = new  File(mCurrentPhotoPath);
                if(imgFile.exists()){
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.imageView);
                    myImage.setImageBitmap(myBitmap);
                }
            }

        }


//convert bitmap to base64==============
    public void bitmapToBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("base64", Base64.encodeToString(byteArray, Base64.DEFAULT));
    }

    public void base64ToImage() {
        byte[] imageAsBytes = Base64.decode(encodedImg.getBytes(), Base64.DEFAULT);
        decodeImg = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        ImageView pastedImg = (ImageView) findViewById(R.id.imageView2);
        pastedImg.setImageBitmap(decodeImg);


    }

//========================

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
    //=====================
