package com.example.diwakar.popular_movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int position = Integer.parseInt(getIntent().getStringExtra("position"));
        Toast.makeText(DetailsActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}
