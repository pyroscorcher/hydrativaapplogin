package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hydrativa.models.LoginRequest;
import com.example.hydrativa.models.LoginResponse;
import com.example.hydrativa.api.ApiService;
import com.example.hydrativa.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private ApiService apiService;

    EditText login_user, pass_user;
    Button btn_login;
    TextView linkRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        login_user = findViewById(R.id.userlogin);
        pass_user = findViewById(R.id.passlogin);
        btn_login = findViewById(R.id.loginButton);
        linkRegister = findViewById(R.id.registerLink);

        // Login button onClick listener
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = login_user.getText().toString().trim();
                String password = pass_user.getText().toString().trim();
                loginUser(username, password);
            }
        });

        // Register link onClick listener
        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginUser(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(Login.this, "Login Successful! Token: " + loginResponse.getToken(), Toast.LENGTH_SHORT).show();
                    // Go to Dashboard or store token
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Login Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
