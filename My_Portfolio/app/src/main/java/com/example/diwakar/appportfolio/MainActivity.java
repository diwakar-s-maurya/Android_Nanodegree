package com.example.diwakar.appportfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showToast(View view) {
        String text = "";
        switch (view.getId()) {
            case R.id.app1:
                text = getResources().getString(R.string.app1);
                break;
            case R.id.app2:
                text = getResources().getString(R.string.app2);
                break;
            case R.id.app3:
                text = getResources().getString(R.string.app3);
                break;
            case R.id.app4:
                text = getResources().getString(R.string.app4);
                break;
            case R.id.app5:
                text = getResources().getString(R.string.app5);
                break;
            case R.id.app6:
                text = getResources().getString(R.string.app6);
                break;
        }
        Toast.makeText(MainActivity.this, "This button will launch my " + text + " app!", Toast.LENGTH_SHORT).show();
    }
}
