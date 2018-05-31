package pages;

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

public class MKBPage implements Page {
    private final static Logger LOG = LogManager.getLogger(AlfabankPage.class.getName());
    private final static String BANK = "Московский кредитный банк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://mkb.ru/exchange-rate");
    }

    private List<ExchangeRates> getInfoAboutCurrencies(){
        List<ExchangeRates> list = new ArrayList<>();
        String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY", "CNY"};

        List<SelenideElement> elements = $(By.cssSelector("div.table-container.table-container_flow.currency-exchange__list")).$(By.tagName("tbody")).$$(By.tagName("tr"));

        for (int i = 1; i < elements.size(); i++){
            boolean isFind = false;

            List<SelenideElement> elements1 = elements.get(i).$$(By.tagName("td"));

            String currency = elements1.get(0).getText();

            int unit = 1;
            for (int j = 0; j < currencies.length; j++){
                if(currency.contains(currencies[j])){
                    if(currencies[j].equals("JPY")){
                        unit = 100;
                        currency = "JPY";
                    }
                    isFind = true;
                    break;
                }
            }
            if(!isFind)continue;

            Double buy  = Double.parseDouble( elements1.get(1).getText());
            Double sell  = Double.parseDouble(elements1.get(2).getText());

            ExchangeRates exchangeRates = new ExchangeRates(null, currency, sell/unit, buy/unit, BANK);
            list.add(exchangeRates);
        }

        return list;
    }

    @Override
    public List<ExchangeRates> getListExchangeRates() {
        initialization();
        return getInfoAboutCurrencies();
    }
}
