package com.secretcitylabs.digitaldna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoadData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        getSupportActionBar().hide();
    }
}