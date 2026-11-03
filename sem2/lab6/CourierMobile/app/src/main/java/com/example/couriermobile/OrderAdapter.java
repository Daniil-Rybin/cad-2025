package com.example.couriermobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter
        extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder, int position) {

        Order order = orders.get(position);

        holder.title.setText(
                "Заказ №" + order.getId()
                        + " — " + order.getStatus()
        );

        holder.details.setText(
                "Клиент: " + order.getClientName() + "\n"
                        + "Откуда: " + order.getPickupAddress() + "\n"
                        + "Куда: " + order.getDeliveryAddress() + "\n"
                        + "Описание: " + order.getDescription() + "\n"
                        + "Категория: " + order.getCategory() + "\n"
                        + "Вес: " + order.getWeight() + " кг\n"
                        + "Курьер: " + order.getCourierName()
        );
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView details;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textOrderTitle);
            details = itemView.findViewById(R.id.textOrderDetails);
        }
    }
}
