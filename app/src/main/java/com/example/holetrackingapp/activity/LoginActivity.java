package com.example.holetrackingapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.holetrackingapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.usernameET);
        loginBtn = findViewById(R.id.loginBtn);

        // loginBtn.setVisibility(View.INVISIBLE);

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 1);

        loginBtn.setOnClickListener(
                (v) -> {
                    String user = usernameET.getText().toString();
                    if(user==null || user.isEmpty()){
                        Toast.makeText(this, "Por favor, ingrese un nombre de usuario",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(this, MapsActivity.class);
                        i.putExtra("user", user);
                        startActivity(i);
                    }
                }
        );
    }
}