
package com.example.couriermobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private TextView textStatus;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        textStatus = findViewById(R.id.textStatus);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString();

        if (username.isEmpty()) {
            editUsername.setError("Введите логин");
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Введите пароль");
            return;
        }

        String credentials = username + ":" + password;

        String encoded = Base64.encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8),
                Base64.NO_WRAP
        );

        String authHeader = "Basic " + encoded;

        buttonLogin.setEnabled(false);
        textStatus.setText("Подключение к серверу...");

        CourierApi api = ApiClient.getClient()
                .create(CourierApi.class);

        api.getCurrentUser(authHeader)
                .enqueue(new Callback<CurrentUser>() {

                    @Override
                    public void onResponse(
                            Call<CurrentUser> call,
                            Response<CurrentUser> response) {

                        buttonLogin.setEnabled(true);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            CurrentUser user = response.body();

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    OrdersActivity.class
                            );

                            intent.putExtra("username", user.getUsername());
                            intent.putExtra("role", user.getRole());
                            intent.putExtra("authHeader", authHeader);

                            editPassword.setText("");
                            startActivity(intent);

                        } else if (response.code() == 401) {
                            textStatus.setText("Неверный логин или пароль");
                        } else {
                            textStatus.setText(
                                    "Ошибка сервера: " + response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<CurrentUser> call,
                            Throwable t) {

                        buttonLogin.setEnabled(true);
                        textStatus.setText(
                                "Ошибка подключения: " + t.getMessage()
                        );
                    }
                });
    }
}
