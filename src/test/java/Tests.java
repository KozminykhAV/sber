import org.junit.jupiter.api.Test;

public class Tests extends TestBase {

    @Test
    void menuTest() {
        BankPO bankPO = new BankPO(driver);
        bankPO.menuCollection();
    }

    @Test
    void pageTest1(){
        BankPO bankPO = new BankPO(driver, "https://www.sberbank-ast.ru/");
        bankPO.goTo("Продажи", "Продажа долгов");
    }

    @Test
    void pageTest2() {
        BankPO bankPO = new BankPO(driver, "https://www.sberbank-ast.ru/");
        bankPO.goTo("Обучение", "График семинаров");
    }
}
