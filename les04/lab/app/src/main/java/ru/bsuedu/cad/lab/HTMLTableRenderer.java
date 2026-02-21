package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.List;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {
        try (FileWriter writer = new FileWriter("product.html")) {

            writer.write("<html><body>");
            writer.write("<table border='1'>");
            writer.write("<tr><th>ID</th><th>Название</th><th>Цена</th><th>Остаток</th></tr>");

            for (Product p : products) {
                writer.write("<tr>");
                writer.write("<td>" + p.getId() + "</td>");
                writer.write("<td>" + p.getName() + "</td>");
                writer.write("<td>" + p.getPrice() + "</td>");
                writer.write("<td>" + p.getStockQuantity() + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</body></html>");

            System.out.println("HTML файл создан: product.html");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
