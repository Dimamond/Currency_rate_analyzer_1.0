package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import static com.codeborne.selenide.Selenide.*;


public class UnicreditbankPage implements Page {
    private final static Logger LOG = LogManager.getLogger(UnicreditbankPage.class.getName());
    private final static String BANK = "ЮниКредит Банк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.unicreditbank.ru/ru/personal/all-currencies.html");
    }

    private Date getDateOfRelevance(){

        String dateTemp = $(By.cssSelector("input.date.form-control")).getValue();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfRelevance = null;
        try {
            dateOfRelevance = dateFormat.parse(dateTemp);
        } catch (ParseException e) {
            LOG.error(e.getMessage());
        }

        return dateOfRelevance;
    }

    private List<ExchangeRates> getInfoAboutCurrencies(){
        List<ExchangeRates> list = new ArrayList<>();
        String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY", "CNY"};


        List<SelenideElement> elements = $(By.cssSelector("div.table_wrap")).$(By.tagName("tbody")).$$(By.tagName("tr"));

        for(SelenideElement se : elements){
            List<SelenideElement> elements1 = se.$$(By.tagName("td"));
            String currency = elements1.get(0).getText();
            String currency1 = elements1.get(1).getText();

            if(!Arrays.asList(currencies).contains(currency) || !currency1.trim().equals("RUB")){
                continue;
            }

            Double buy  = Double.parseDouble( elements1.get(3).getText());
            Double sell  = Double.parseDouble(elements1.get(4).getText());

            ExchangeRates exchangeRates = new ExchangeRates(getDateOfRelevance(), currency, sell, buy, BANK);
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
