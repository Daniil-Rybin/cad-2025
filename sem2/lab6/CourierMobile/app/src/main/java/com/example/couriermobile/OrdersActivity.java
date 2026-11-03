
package com.example.couriermobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private TextView textMessage;
    private String authHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        TextView textWelcome = findViewById(R.id.textWelcome);
        TextView textRole = findViewById(R.id.textRole);
        textMessage = findViewById(R.id.textMessage);

        Button buttonRefresh = findViewById(R.id.buttonRefresh);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(
                new LinearLayoutManager(this)
        );

        String username = getIntent().getStringExtra("username");
        String role = getIntent().getStringExtra("role");
        authHeader = getIntent().getStringExtra("authHeader");

        if (authHeader == null || role == null) {
            finish();
            return;
        }

        textWelcome.setText("Здравствуйте, " + username + "!");

        switch (role) {
            case "USER":
                textRole.setText("Роль: Клиент");
                break;
            case "COURIER":
                textRole.setText("Роль: Курьер");
                break;
            case "MODERATOR":
                textRole.setText("Роль: Администратор");
                break;
            default:
                textRole.setText("Роль: " + role);
        }

        buttonRefresh.setOnClickListener(v -> loadOrders());

        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        });

        loadOrders();
    }

    private void loadOrders() {

        textMessage.setText("Загрузка заказов...");

        CourierApi api = ApiClient.getClient()
                .create(CourierApi.class);

        api.getOrders(authHeader).enqueue(
                new Callback<List<Order>>() {

                    @Override
                    public void onResponse(
                            Call<List<Order>> call,
                            Response<List<Order>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Order> orders = response.body();

                            recyclerOrders.setAdapter(
                                    new OrderAdapter(orders)
                            );

                            textMessage.setText(
                                    orders.isEmpty()
                                            ? "Заказов пока нет"
                                            : "Всего заказов: " + orders.size()
                            );

                        } else {
                            textMessage.setText(
                                    "Ошибка загрузки: " + response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Order>> call,
                            Throwable t) {

                        textMessage.setText(
                                "Нет соединения: " + t.getMessage()
                        );
                    }
                }
        );
    }
}
