import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

public class BankPO {

    private final WebDriver driver;
    private final WebElement menuBar;
    private final String mainPage = "https://www.sberbank-ast.ru/";
    private final String barXpath = "//*[@class='master_open_menu']";
    private final Map<String, Map<String, String>> menu = new HashMap<>();

    public BankPO(WebDriver driver) {
        this.driver = driver;
        driver.get(mainPage);
        menuBar = driver.findElement(By.xpath(barXpath));
    }

    public BankPO(WebDriver driver, String url) {
        this.driver = driver;
        driver.get(url);
        menuBar = driver.findElement(By.xpath(barXpath));
    }

    // Метод, который собирает меню страницы из доступных разделов и подразделов
    void menuCollection() {
        // Составление списка всех пунктов меню
        List<WebElement> menuButtons = menuBar.findElements(By.xpath("//li//span"));

        // Предварительная структура меню в виде пар <Название меню, Веб-элемент подменю>
        Map<String, List<WebElement>> preliminaryMenu = new HashMap<>();
        menuButtons.forEach(e -> preliminaryMenu.put(e.getText(), e.findElements(By.xpath
                ("//li/span[contains(text(), '" + e.getText() + "')]//parent::li//a[@href]"))));

        // Основа для конечной версии меню (представлена в виде пар <Название меню, Пустая HashMap>)
        preliminaryMenu.forEach((key, value) -> value.forEach(e -> menu.put(key, new HashMap<>())));

        // Заполнение меню. Готовое меню будет представлено в виде HashMap<Меню, HashMap<Подменю, URL-ссылка>>
        preliminaryMenu.forEach((key, value) -> {
            Map<String, String> temp = value.stream()
                    .collect(Collectors.toMap(e -> e.getAttribute("innerHTML"), e -> e.getAttribute("href")));
            menu.put(key, temp);
        });

        // Перейдем в каждый пункт-подпункт, возвращаясь на главную страницу после каждого перехода
        // Структуру меню со ссылками для наглядности выведем в консоль
        menu.forEach((key, value) -> value.forEach((key1, value1) -> {
            goTo(key, key1);
            driver.get(mainPage);
            System.out.println(key + " : " + key1 + " : " + value1);
        }));
    }

    // Метод для перехода в нужный нам пункт меню
    void goTo(String menu, String subMenu) {
        WebElement element = driver.findElement(By.xpath("//span[contains(text(), '" + menu + "')]/parent::li"));
        String link = element.findElement(By.xpath("//a[contains(text(), '" + subMenu + "')]")).getAttribute("href");
        driver.get(link);
    }
}
