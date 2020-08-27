package com.app.rodigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Confirm extends AppCompatActivity {

    Button rbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        rbutton = findViewById(R.id.button);
        //nbutton = findViewById(R.id.button2);

        rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Confirm.this.finish();

            }
        });
        /*nbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm.this.finish();
                System.exit(0);
            }*/
        //});
    }
}
