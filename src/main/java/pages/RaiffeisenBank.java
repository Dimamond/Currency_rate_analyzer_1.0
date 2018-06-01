package pages;

import com.codeborne.selenide.Configuration;
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

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class RaiffeisenBank implements Page {
    private final static Logger LOG = LogManager.getLogger(RaiffeisenBank.class.getName());
    private final static String BANK = "Райффайзен Банк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.raiffeisen.ru/currency_rates/");
    }

    private Date getDateOfRelevance(){

        String dateTemp = $(By.cssSelector("div.b-block-text-container")).getText().replace(" МСК ", " ").substring(18);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
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

        List<SelenideElement> elements = $$(By.cssSelector("div.b-table__row"));
        for (SelenideElement se : elements){
            List<SelenideElement> elements1 = se.$$(By.tagName("div"));
            String currency = elements1.get(0).getText();
            if(!Arrays.asList(currencies).contains(currency))continue;

            Double buy  = Double.parseDouble( elements1.get(3).getText());
            Double sell  = Double.parseDouble( elements1.get(4).getText());

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
