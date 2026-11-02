
package com.example.courierclient;

import com.fasterxml.jackson.databind.JsonNode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class CourierManagerForm extends JFrame {

    private final ApiClient api;
    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel statusLabel;

    public CourierManagerForm(ApiClient api) {
        this.api = api;

        setTitle("Управление доставками");
        setSize(1200, 650);
        setMinimumSize(new Dimension(850, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 10, 20)
        );

        JLabel titleLabel = new JLabel("Управление заявками");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel userLabel = new JLabel(
                "Пользователь: " + api.getUsername()
        );

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Клиент",
                "Адрес отправления",
                "Адрес доставки",
                "Описание",
                "Категория",
                "Вес (кг)",
                "Статус",
                "Курьер"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return Long.class;
                }

                if (column == 6) {
                    return Double.class;
                }

                return String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(model);

        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(190);
        table.getColumnModel().getColumn(3).setPreferredWidth(190);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        table.getColumnModel().getColumn(8).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 15, 20)
        );

        statusLabel = new JLabel("Загрузка заявок");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        JButton addButton = new JButton("Добавить заявку");
        JButton deleteButton = new JButton("Удалить заявку");
        JButton refreshButton = new JButton("Обновить");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteSelectedOrder());
        refreshButton.addActionListener(e -> loadOrders());

        loadOrders();
    }

    private void loadOrders() {
        statusLabel.setText("Загрузка заявок");

        SwingWorker<JsonNode, Void> worker = new SwingWorker<>() {

            @Override
            protected JsonNode doInBackground() throws Exception {
                return api.getOrders();
            }

            @Override
            protected void done() {
                try {
                    JsonNode orders = get();
                    model.setRowCount(0);

                    for (JsonNode order : orders) {
                        model.addRow(new Object[]{
                                order.path("id").asLong(),
                                order.path("clientName").asText(""),
                                order.path("pickupAddress").asText(""),
                                order.path("deliveryAddress").asText(""),
                                order.path("description").asText(""),
                                order.path("category").asText(""),
                                order.path("weight").asDouble(),
                                order.path("status").asText(""),
                                order.path("courierName").asText("")
                        });
                    }

                    statusLabel.setText(
                            "Всего заявок: " + model.getRowCount()
                    );

                } catch (Exception e) {
                    statusLabel.setText("Ошибка загрузки");

                    showError(e);
                }
            }
        };

        worker.execute();
    }

    private void showAddDialog() {
        JTextField clientField = new JTextField();
        JTextField pickupField = new JTextField();
        JTextField deliveryField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField weightField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 12));

        panel.add(new JLabel("Имя клиента:"));
        panel.add(clientField);

        panel.add(new JLabel("Адрес отправления:"));
        panel.add(pickupField);

        panel.add(new JLabel("Адрес доставки:"));
        panel.add(deliveryField);

        panel.add(new JLabel("Описание:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Категория:"));
        panel.add(categoryField);

        panel.add(new JLabel("Вес (кг):"));
        panel.add(weightField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Создание новой заявки",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String clientName = clientField.getText().trim();
            String pickupAddress = pickupField.getText().trim();
            String deliveryAddress = deliveryField.getText().trim();
            String description = descriptionField.getText().trim();
            String category = categoryField.getText().trim();

            double weight = Double.parseDouble(
                    weightField.getText().trim().replace(",", ".")
            );

            if (clientName.isEmpty()
                    || pickupAddress.isEmpty()
                    || deliveryAddress.isEmpty()) {
                throw new IllegalArgumentException(
                        "Заполните все обязательные поля"
                );
            }

            if (!Double.isFinite(weight) || weight <= 0) {
                throw new IllegalArgumentException(
                        "Вес должен быть больше нуля"
                );
            }

            api.addOrder(
                    clientName,
                    pickupAddress,
                    deliveryAddress,
                    description,
                    category,
                    weight
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Заявка успешно создана",
                    "Успешно",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadOrders();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Укажите корректный вес, например 2.5",
                    "Ошибка ввода",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Выберите заявку для удаления",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);

        long id = ((Number) model.getValueAt(
                modelRow, 0
        )).longValue();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Вы действительно хотите удалить заявку №" + id + "?",
                "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            api.deleteOrder(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Заявка №" + id + " успешно удалена",
                    "Успешно",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadOrders();

        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        Throwable cause = e.getCause();

        String message = cause != null
                ? cause.getMessage()
                : e.getMessage();

        JOptionPane.showMessageDialog(
                this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
