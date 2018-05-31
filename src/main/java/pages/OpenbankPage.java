package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class OpenbankPage implements Page {
    private final static Logger LOG = LogManager.getLogger(AlfabankPage.class.getName());
    private final static String BANK = "ФК Открытие";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.open.ru/exchange-person");
    }

    private void chooseCityMoscow(){
        $(By.cssSelector("a.small-text.town__value")).click();
        List<SelenideElement> elements = $(By.xpath("//*[@id='towns']/div[2]/ul/div[2]")).shouldBe(Condition.visible).$$(By.tagName("li"));

        for (SelenideElement element : elements){
            SelenideElement element1 = element.$(By.cssSelector("a.city"));
            if(element1.getText().equals("Москва")){
                element1.click();
                break;
            }
        }
    }

    private List<ExchangeRates> getInfoAboutCurrencies(){
        List<ExchangeRates> list = new ArrayList<>();
        String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY", "CNY"};

        List<SelenideElement> elements = $(By.cssSelector("table.currency-exchange-table")).$(By.tagName("tbody")).$$(By.tagName("tr"));

        for (SelenideElement se : elements){
            List<SelenideElement> elements1 = se.$$(By.tagName("td"));
            String currency = elements1.get(0).getText();
            if(!Arrays.asList(currencies).contains(currency)){
                continue;
            }
            Double buy  = Double.parseDouble( elements1.get(2).getText().replace(",", "."));
            Double sell  = Double.parseDouble(elements1.get(1).getText().replace(",", "."));
            ExchangeRates exchangeRates = new ExchangeRates(null, currency, sell, buy, BANK);
            list.add(exchangeRates);
        }
        return list;
    }

    @Override
    public List<ExchangeRates> getListExchangeRates() {
        initialization();
        chooseCityMoscow();
        return getInfoAboutCurrencies();
    }
}
