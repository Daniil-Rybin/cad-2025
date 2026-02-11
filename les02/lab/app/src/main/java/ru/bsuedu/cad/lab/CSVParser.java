package ru.bsuedu.cad.lab;
import java.util.*;
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(List<String> lines) {
        return lines.stream()
                .skip(1) // пропускаем заголовок
                .filter(line -> !line.isBlank())
                .map(line -> {
                    String[] parts = line.split(",");

                    if (parts.length < 9) {
                        throw new RuntimeException(
                                "Некорректная строка CSV: " + line
                        );
                    }

                    return new Product(
                            Long.parseLong(parts[0].trim()),   // product_id
                            parts[1].trim(),                   // name
                            parts[2].trim(),                   // description
                            Double.parseDouble(parts[4].trim()),// price
                            Integer.parseInt(parts[5].trim())  // stock_quantity
                    );
                })
                .toList();
    }
}

