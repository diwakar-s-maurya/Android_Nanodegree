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

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.app1:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                    case R.id.app2:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                    case R.id.app3:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                    case R.id.app4:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                    case R.id.app5:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                    case R.id.app6:
                        showToast("This button will launch my " + getResources().getString(R.string.app1) + " app!");
                        break;
                }
            }
        };

        ((Button) findViewById(R.id.app1)).setOnClickListener(l);
        ((Button) findViewById(R.id.app2)).setOnClickListener(l);
        ((Button) findViewById(R.id.app3)).setOnClickListener(l);
        ((Button) findViewById(R.id.app4)).setOnClickListener(l);
        ((Button) findViewById(R.id.app5)).setOnClickListener(l);
        ((Button) findViewById(R.id.app6)).setOnClickListener(l);

    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
