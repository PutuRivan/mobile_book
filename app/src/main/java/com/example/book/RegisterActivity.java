package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btn_create);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent moveHome = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(moveHome);
    }
}