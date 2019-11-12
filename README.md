Access Camera on Android device to take picture and store it

Step 1: 
In the manifests folder, open the file AndroidManifest.xml.
This will be the place we grant permission for the application to access camera :
Take the code below and put it under the manifest tag

<uses_feature android:name=“android.hardware.camera” android: required=“true” />

Noted: if your application can not function without the camera you should leave the part android:required=“true” otherwise set it to false


And this line
<uses-permission android:name=“android.permission.WRITE_EXTERNAL_STORAGE"/>






Step 2:

In the MainActivity.java:
In order to save an image file you have to create a new file and give it a name as below

String currentPhotoPath;

Then add the code below:
//==========
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

  Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
        }
//============

Then back to Manifest file to add a provider for the application

 <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.example.android.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"></meta-data>
 </provider>


Next, we create a folder in the res folder and name it xml then we create a xml file called file_paths and put this code below . Remember to replace the part com.example.cameraapp with your package name

<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.example.cameraapp/files/Pictures" />
</paths>



Step 3:
Again go to MainActivity.java
create a variable:

static final int REQUEST_TAKE_PHOTO = 1;

This will be used as a unique identifier by the code to take a photo

Step 4: 

We create a method dispatchTakePictureIntent()

For the method, we put the code below:

//========================
private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
//        }
    }
//============================
 In this function we invoke the Intent to open the camera
Then call the startActivityForResult()  method to take the result from the intent which is an image.

Step 5:

We override this method:

  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageBitmap(myBitmap);
            }
        }
    }
