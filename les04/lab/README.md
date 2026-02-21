# Отчет о лабораторной работе №2
## Выполнение работы
1. Новый класс HTMLTableRenderer
```
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
```
2. Новый класс ParsingTimeAspect
```
package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParsingTimeAspect
{
    @Around("execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        System.out.println("Время парсинга CSV: "
                + (end - start) + " мс");

        return result;
    }
}
```
3. Сборка проекта
```
12:57:45: Executing 'run'…

Reusing configuration cache.
> Task :app:processResources
> Task :app:compileJava FROM-CACHE
> Task :app:classes

> Task :app:run
ResourceFileReader инициализирован: 2026-02-21T12:57:47.116496100
Время парсинга CSV: 1 мс
HTML файл создан: product.html

BUILD SUCCESSFUL in 1s
3 actionable tasks: 2 executed, 1 from cache
Configuration cache entry reused.
12:57:47: Execution finished 'run'.
```
4. Вывод в HTML-файл
![img.png](img.png)
<div>Рисунок 1 - Вывод в HTML-файл</div>

## Реализация

## Результат работы
Обновленная диаграмма классов
```mermaid
classDiagram
    note "Товары для зоомагазина"
    
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    ConsoleTableRenderer ..> ProductProvider
    HTMLTableRenderer ..> ProductProvider
    ProductProvider ..> Product
    Parser ..> Product
    App ..> ProductProvider
    App ..> Renderer
    AppConfig ..> App
    ParsingTimeAspect ..> CSVParser

    class Product {
        -long id
        -String name
        -String description
        -double price
        -int stockQuantity
        +Product(long, String, String, double, int)
        +getId() long
        +getName() String
        +getDescription() String
        +getPrice() double
        +getStockQuantity() int
    }

    class Reader {
        <<interface>>
        +List~String~ read()
    }

    class ResourceFileReader {
        -String filename
        +init() void
        +read() List~String~
    }

    class Parser {
        <<interface>>
        +List~Product~ parse(List~String~)
    }

    class CSVParser {
        +parse(List~String~) List~Product~
    }

    class Renderer {
        <<interface>>
        +render(List~Product~) void
    }

    class ConsoleTableRenderer {
        +render(List~Product~) void
    }

    class HTMLTableRenderer {
        <<Primary>>
        +render(List~Product~) void
    }

    class ProductProvider {
        <<interface>>
        +List~Product~ getProducts()
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +ConcreteProductProvider(Reader, Parser)
        +getProducts() List~Product~
    }

    class App {
        +main(String[]) void$
    }

    class AppConfig {
        <<Configuration>>
        <<ComponentScan>>
        <<EnableAspectJAutoProxy>>
        <<PropertySource>>
    }

    class ParsingTimeAspect {
        <<Aspect>>
        +measureTime(ProceedingJoinPoint) Object
    }
```