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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Report1 extends AppCompatActivity implements LocationListener  {
    Button mlogout, msubmit;

    Uri contentUri;
    File f;
    MultiAutoCompleteTextView mWrite;
    String imageFileName;
    Button rbutton;
    ProgressBar mprogressBar;

    Spinner s1, s2, s3;

    TextView t1, t2;
    ArrayList<String> arrayList_s1;
    ArrayAdapter<String> arrayAdapter_s1, arrayAdapter_s2, arrayAdapter_s3;

    ArrayList<String> arrayList_north, arrayList_south;
    ArrayList<String> a1;

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
        setContentView(R.layout.activity_report1);
        mlat = (TextView) findViewById(R.id.lat);
        mlng = (TextView) findViewById(R.id.lng);
        msubmit = findViewById(R.id.submit);
        storageReference = FirebaseStorage.getInstance().getReference();
        mlogout = findViewById(R.id.logout);
       // mpic = (ImageView) findViewById(R.id.pics);
        Button fab = findViewById(R.id.fab);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fAuth = FirebaseAuth.getInstance();
        mWrite = findViewById(R.id.write);

        fstore = FirebaseFirestore.getInstance();
        rbutton = findViewById(R.id.button6);

        s1 = (Spinner) findViewById(R.id.spinner);
        s2 = (Spinner) findViewById(R.id.spinner2);
        s3 = (Spinner) findViewById(R.id.spinner3);
        mprogressBar = findViewById(R.id.progressBar2);



        fstore = FirebaseFirestore.getInstance();


        t1 = (TextView) findViewById(R.id.textView6);
        t2 = (TextView) findViewById(R.id.textView8);
        mWrite = (MultiAutoCompleteTextView) findViewById(R.id.write);

        arrayList_s1 = new ArrayList<>();
        arrayList_s1.add("North Goa");
        arrayList_s1.add("South Goa");

        arrayAdapter_s1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_s1);

        s1.setAdapter(arrayAdapter_s1);

        arrayList_north = new ArrayList<>();
        arrayList_north.add("Assonora");
        arrayList_north.add("Birondam");
        arrayList_north.add("Candolim");
        arrayList_north.add("Curca");
        arrayList_north.add("Mauzi");
        //arrayList_north.add("Others");

        arrayList_south = new ArrayList<>();
        arrayList_south.add("A.p Terminal");
        arrayList_south.add("Ambaulim");
        arrayList_south.add("Bhatpal");
        arrayList_south.add("Calem");
        arrayList_south.add("Priol");
        //arrayList_south.add("Others");

        a1 = new ArrayList<>();
        a1.add("Road no. 1");
        a1.add("Road no. 2");
        a1.add("Road no. 3");
        a1.add("Road no. 4");
        a1.add("Road no. 5");
        a1.add("Road no. 6");
        a1.add("Road no. 7");
        a1.add("Road no. 8");
        a1.add("Road no. 9");
        a1.add("Road no. 10");
        a1.add("Others");


        mWrite.setHint("");

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    t1.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    arrayAdapter_s2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_north);


                } else if (position == 1) {
                    t1.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    arrayAdapter_s2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_south);


                }
                s2.setAdapter(arrayAdapter_s2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> s1) {

            }
        });


        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {





            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapter_s3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, a1);
                if(position == 5) {
                    mWrite.setHint("Enter Area, Road and Landmark");


                }else{
                    t2.setVisibility(View.VISIBLE);
                    s3.setVisibility(View.VISIBLE);
                }




                s3.setAdapter(arrayAdapter_s3);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 10) {
                    mWrite.setHint("Enter Road and Landmark");


                }
                else{
                    mWrite.setHint("Landmark");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String District = s1.getSelectedItem().toString();
                String Area = s2.getSelectedItem().toString();
                final String Roads = s3.getSelectedItem().toString();
                final String Landmark = mWrite.getText().toString();

                if(TextUtils.isEmpty(District)){
                    Toast.makeText(Report1.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Area)){

                    Toast.makeText(Report1.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Roads)){
                    Toast.makeText(Report1.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Area)){
                    Toast.makeText(Report1.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }


                startActivity(new Intent(getApplicationContext(),Report.class));

            }
        });*/


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
                        Uri photoURI = (Uri) FileProvider.getUriForFile(Report1.this,
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
                   // startActivity(new Intent(getApplicationContext(), Report1.class));
                    final String Lat = mlat.getText().toString().trim();
                    final String lng = mlng.getText().toString().trim();

                    final String District = s1.getSelectedItem().toString();
                    String Area = s2.getSelectedItem().toString();
                    final String Roads = s3.getSelectedItem().toString();
                    final String Landmark = mWrite.getText().toString();



                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());





                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("UserReport").document(District).collection(Area).document(Roads).collection(userID).document();

                    Map<String,Object> user = new HashMap<>();
                    user.put("image : ", Uri.fromFile(f).toString());
                    user.put("",Lat);
                    user.put("",lng);
                    user.put("report : ", Landmark);
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

                    //Report1.this.finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Report1.class));
                    Toast.makeText(Report1.this, "image is not captured or written report is empty", Toast.LENGTH_SHORT).show();
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
            //mpic.setImageURI(Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);





        }
    }

    private void uploadImage(String name, Uri contentUri) {
        mprogressBar.setVisibility(View.VISIBLE);

        final StorageReference image = storageReference.child("images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Report1.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(Report1.this, "uploading done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Confirm.class));





                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Report1.this, "uploading failed", Toast.LENGTH_SHORT).show();
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
