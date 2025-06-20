package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        EditText usernameEditText = findViewById(R.id.register_username_input);
        EditText emailEditText = findViewById(R.id.register_email_input);

        btnLogin = findViewById(R.id.btn_login);

        // kalo pencet logout, nanti di login email sama usernameny udah keisi (remember me)
        Intent getProfile = getIntent();
        String getusername = getProfile.getStringExtra("username");
        String getemail = getProfile.getStringExtra("email");

        if (getusername != null) {
            usernameEditText.setText(getusername);
        }
        if (getemail != null) {
            emailEditText.setText(getemail);
        }


        registerText = findViewById(R.id.registertext);
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}