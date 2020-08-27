package com.app.rodigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Choice extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        rg = (RadioGroup) findViewById(R.id.irg);

    }
    public void rbclick(View view)
    {
        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radiobuttonid);

        if(radiobuttonid == R.id.radioButton)
            startActivity(new Intent(getApplicationContext(),Intro.class));
        else
            startActivity(new Intent(getApplicationContext(),Admin.class));

    }
}
