
package com.example.courierclient;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginForm() {
        setTitle("Авторизация");
        setSize(420, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(
                "Служба доставки",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 30, 30)
        );

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Войти");

        formPanel.add(new JLabel("Логин:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Пароль:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());

        getRootPane().setDefaultButton(loginButton);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Введите логин и пароль",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Подключение");

        SwingWorker<ApiClient, Void> worker = new SwingWorker<>() {

            @Override
            protected ApiClient doInBackground() throws Exception {
                ApiClient api = new ApiClient(username, password);
                api.getOrders();
                return api;
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Войти");

                try {
                    ApiClient api = get();

                    CourierManagerForm managerForm =
                            new CourierManagerForm(api);

                    managerForm.setVisible(true);
                    dispose();

                } catch (Exception e) {
                    Throwable cause = e.getCause();
                    String message = cause != null
                            ? cause.getMessage()
                            : e.getMessage();

                    JOptionPane.showMessageDialog(
                            LoginForm.this,
                            message,
                            "Ошибка авторизации",
                            JOptionPane.ERROR_MESSAGE
                    );

                    passwordField.setText("");
                    passwordField.requestFocusInWindow();
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new LoginForm().setVisible(true);
        });
    }
}
