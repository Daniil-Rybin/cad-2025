
package com.example.courierclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

public class ApiClient {

    private static final String API_URL = "http://localhost:8080/api/orders";

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String authorization;
    private final String username;

    public ApiClient(String username, String password) {
        this.username = username;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.mapper = new ObjectMapper();

        String credentials = username + ":" + password;

        this.authorization = "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsername() {
        return username;
    }

    public JsonNode getOrders() throws Exception {
        HttpRequest request = createRequest(API_URL)
                .GET()
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            throw new Exception("Ошибка загрузки заявок: "
                    + response.statusCode());
        }

        return mapper.readTree(response.body());
    }

    public void addOrder(
            String clientName,
            String pickupAddress,
            String deliveryAddress,
            String description,
            String category,
            double weight
    ) throws Exception {

        OrderRequest order = new OrderRequest(
                clientName,
                pickupAddress,
                deliveryAddress,
                description,
                category,
                weight
        );

        String json = mapper.writeValueAsString(order);

        HttpRequest request = createRequest(API_URL)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        json,
                        StandardCharsets.UTF_8
                ))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200
                && response.statusCode() != 201) {
            throw new Exception("Ошибка создания заявки: "
                    + response.statusCode()
                    + "\n" + response.body());
        }
    }

    public void deleteOrder(long id) throws Exception {
        HttpRequest request = createRequest(API_URL + "/" + id)
                .DELETE()
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 403) {
            throw new Exception(
                    "Удаление заявок доступно только модератору"
            );
        }

        if (response.statusCode() == 404) {
            throw new Exception("Заявка не найдена");
        }

        if (response.statusCode() != 204) {
            throw new Exception("Ошибка удаления: "
                    + response.statusCode()
                    + "\n" + response.body());
        }
    }

    private HttpRequest.Builder createRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Authorization", authorization);
    }

    private HttpResponse<String> send(HttpRequest request)
            throws Exception {

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() == 401) {
            throw new Exception("Неверный логин или пароль");
        }

        return response;
    }

    private record OrderRequest(
            String clientName,
            String pickupAddress,
            String deliveryAddress,
            String description,
            String category,
            double weight
    ) {}
}
