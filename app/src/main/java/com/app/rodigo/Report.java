package com.app.rodigo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.DescriptorProtos;
import com.google.type.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Report extends AppCompatActivity implements LocationListener  {
    Button mlogout, msubmit;
    ImageView mpic;
    Uri contentUri;
    File f;
    MultiAutoCompleteTextView mWrite;
    String imageFileName;

    private LocationManager locationManager;
    TextView mlat, mlng;
    private static final int CAMERA_REQUEST_CODE = 1;
    String currentPhotoPath;
    private StorageReference storageReference;
    String name,userID,url;
    FirebaseFirestore fstore;

    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mlat = (TextView) findViewById(R.id.lat);
        mlng = (TextView) findViewById(R.id.lng);
        msubmit = findViewById(R.id.submit);
        storageReference = FirebaseStorage.getInstance().getReference();
        mlogout = findViewById(R.id.logout);
        mpic = (ImageView) findViewById(R.id.pics);
        Button fab = findViewById(R.id.fab);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fAuth = FirebaseAuth.getInstance();
        mWrite = findViewById(R.id.write);

        fstore = FirebaseFirestore.getInstance();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        final Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d("exception", "cant create file");

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = (Uri) FileProvider.getUriForFile(Report.this,
                                "com.app.rodigo.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

                        onLocationChanged(location);


                    }
                }


            }
        });

        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String report = mWrite.getText().toString().trim();
                if ( TextUtils.isEmpty(report)) {
                    mWrite.setError("this field is empty");
                    return;
                }
                if (contentUri != null ) {
                    uploadImage(f.getName(), contentUri);

                    Log.d(String.valueOf(this),"ONFAILURE" +url);
                    startActivity(new Intent(getApplicationContext(), Report.class));
                    final String Lat = mlat.getText().toString().trim();
                    final String lng = mlng.getText().toString().trim();



                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());





                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("UserReport").document("District").collection("Area").document("Roads");

                    Map<String,Object> user = new HashMap<>();
                    user.put("image : ", Uri.fromFile(f).toString());
                    user.put("UID :",userID.toString());
                    user.put("report : ", report);
                    user.put("Date :", timeStamp);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(String.valueOf(this),"on success" + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(String.valueOf(this),"ONFAILURE" +e.toString());
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), Report.class));
                    Toast.makeText(Report.this, "image is not captured or written report is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void login(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            f = new File(currentPhotoPath);
            mpic.setImageURI(Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);





        }
    }

    private void uploadImage(String name, Uri contentUri) {

        final StorageReference image = storageReference.child("images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(Report.this, "uploading done", Toast.LENGTH_SHORT).show();




                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Report.this, "uploading failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "img_" + timeStamp   ;
        //  File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onLocationChanged(Location location) {
        double Longitude = location.getLongitude();
        double Latitude = location.getLatitude();
        mlat.setText("Latitude : " + Latitude);
        mlng.setText("Longitude : " + Longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}