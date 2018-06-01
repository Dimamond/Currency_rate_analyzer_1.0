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


public class PsbankPage implements Page {
    private final static Logger LOG = LogManager.getLogger(PsbankPage.class.getName());
    private final static String BANK = "Промсвязьбанк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.psbank.ru/Personal/Everyday/Rates/HappyHours_IB?from=currencyWidget");
    }

    private void footerMessageClose(){
        $(By.cssSelector("div.footer-message-right-close-icon")).click();
    }

    private void nextList(){
        $(By.cssSelector("a.bjqs-next")).click();

    }

    private List<ExchangeRates> getInfoAboutCurrencies(){
        List<ExchangeRates> list = new ArrayList<>();
        String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY", "CNY"};


        List<SelenideElement> elements = $$(By.cssSelector("ul.b-exchange-list"));

        for (int i = 0; i < elements.size(); i++){
            List<SelenideElement> elements1 = elements.get(i).$$(By.cssSelector("li.b-exchange-list__data"));
            for (int j = 0; j < elements1.size(); j++){
                if((j >= 2 && j <= 4) && i == 0)continue;
                List<SelenideElement> elements2 = elements1.get(j).$$(By.tagName("li"));
                String[] temp = elements2.get(0).getText().split(" ");
                String currency = temp[temp.length - 1];
                int unit = Integer.parseInt(temp[0]);
                if(!Arrays.asList(currencies).contains(currency))continue;

                Double buy  = Double.parseDouble( elements2.get(1).getText().replace(",", "."));
                Double sell  = Double.parseDouble( elements2.get(2).getText().replace(",", "."));


                ExchangeRates exchangeRates = new ExchangeRates(null, currency, sell/unit, buy/unit, BANK);
                list.add(exchangeRates);

            }
            nextList();
        }
        return list;
    }


    @Override
    public List<ExchangeRates> getListExchangeRates() {
        initialization();
        footerMessageClose();
        return getInfoAboutCurrencies();
    }
}
