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

import static com.codeborne.selenide.Selenide.*;


public class AlfabankPage implements Page {
    private final static Logger LOG = LogManager.getLogger(AlfabankPage.class.getName());
    private final static String BANK = "Альфа-Банк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://alfabank.ru/currency/");
    }

    private Date getDateOfRelevance(){


        SelenideElement se = $(By.cssSelector("div.title.type-datetime"));

        String dateTemp = String.format("%s %s", se.$(By.cssSelector("span.date.val")).getText(),
                                                 se.$(By.cssSelector("span.time.val")).getText());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
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

        List<SelenideElement> elements = $(By.cssSelector("table.table")).$$(By.tagName("tr"));


        for (int i = 2; i < elements.size(); i++){

            List<SelenideElement> elements1 = elements.get(i).$$(By.tagName("td"));

            String currency = elements1.get(0).getText();

            if(!Arrays.asList(currencies).contains(currency)){
                continue;
            }

            Double buy  = Double.parseDouble( elements1.get(1).getText());
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
