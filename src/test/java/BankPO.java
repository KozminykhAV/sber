import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

public class BankPO {

    private final WebDriver driver;
    private final WebElement menuBar;
    private final Map<String, Map<String, String>> menu = new HashMap<>();

    public BankPO(WebDriver driver) {
        this.driver = driver;
        driver.get("https://www.sberbank-ast.ru/");
        menuBar = driver.findElement(By.xpath("//*[@class='master_open_menu']"));
    }

    /** Метод собирает меню страницы из доступных разделов и подразделов */
    void menuCollection() {

        /* Предварительная структура меню в виде пар <Название меню, Веб-элемент подменю> */
        Map<String, List<WebElement>> preliminaryMenu = new HashMap<>();
        menuBar.findElements(By.xpath("//li//span"))
                .forEach(e -> preliminaryMenu.put(e.getText(), e.findElements(By.xpath
                        ("//*[contains(text(), '" + e.getText() + "')]//parent::li//a[@href]"))));

        /* Заполнение меню. Готовое меню будет представлено в виде HashMap<Меню, HashMap<Подменю, URL-ссылка>> */
        preliminaryMenu.forEach((key, value) -> {
            Map<String, String> temp = value.stream()
                    .collect(Collectors.toMap(e -> e.getAttribute("innerHTML"), e -> e.getAttribute("href")));
            menu.put(key, temp);
        });

        /* Вывод структуры меню в консоль (для наглядности) */
        menu.forEach((key, value) -> value.forEach((subKey, subValue) ->
                System.out.println(key + " : " + subKey + " : " + subValue)
        ));
    }

    /** Метод перехода на страницу по названию пункта и подпункта меню */
    void goTo(String menu, String subMenu) {
        WebElement element = driver.findElement(By.xpath("//span[contains(text(), '" + menu + "')]/parent::li"));
        String link = element.findElement(By.xpath("//a[contains(text(), '" + subMenu + "')]")).getAttribute("href");
        driver.get(link);
    }
}
