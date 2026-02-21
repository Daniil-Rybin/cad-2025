package ru.bsuedu.cad.lab;
import java.util.List;
public class ConsoleTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {
        System.out.printf(
                "%-5s %-30s %-10s %-10s%n",
                "ID", "Название", "Цена", "Остаток"
        );
        System.out.println("-----------------------------------------------------------");

        for (Product p : products) {
            System.out.printf(
                    "%-5d %-30s %-10.2f %-10d%n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStockQuantity()
            );
        }
    }
}


