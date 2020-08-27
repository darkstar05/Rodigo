package com.app.rodigo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Report0 extends AppCompatActivity {
    Button rbutton;

    Spinner s1, s2, s3;
    Button b1;
    TextView t1, t2;
    MultiAutoCompleteTextView mWrite;
    ArrayList<String> arrayList_s1;
    ArrayAdapter<String> arrayAdapter_s1, arrayAdapter_s2, arrayAdapter_s3;

    ArrayList<String> arrayList_north, arrayList_south;
    ArrayList<String> a1;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report0);
        rbutton = findViewById(R.id.button6);

        s1 = (Spinner) findViewById(R.id.spinner);
        s2 = (Spinner) findViewById(R.id.spinner2);
        s3 = (Spinner) findViewById(R.id.spinner3);

        fAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

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


        rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String District = s1.getSelectedItem().toString();
                 String Area = s2.getSelectedItem().toString();
                final String Roads = s3.getSelectedItem().toString();
                final String Landmark = mWrite.getText().toString();

                if(TextUtils.isEmpty(District)){
                    Toast.makeText(Report0.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Area)){

                    Toast.makeText(Report0.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Roads)){
                    Toast.makeText(Report0.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Area)){
                    Toast.makeText(Report0.this, "Please Complete All Entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference documentReference = fstore.collection("UserReport").document(District).collection(Area).document(Roads);


                startActivity(new Intent(getApplicationContext(),Report.class));

            }
        });


    }
}