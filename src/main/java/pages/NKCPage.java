package pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;


public class NKCPage implements Page {
    private final static Logger LOG = LogManager.getLogger(NKCPage.class.getName());
    private final static String BANK = "Национальный клиринговый центр";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("http://www.nkcbank.ru/viewCatalog.do?menuKey=5");
    }


    private Date getDateOfRelevance(SelenideElement se){

        String dateTemp = $$(By.tagName("th")).get(2).getText();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
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

        List<SelenideElement> elements = $(By.cssSelector("table.coursTable")).$$(By.tagName("tr"));

        for (int i = 1; i < elements.size(); i++){
            if(i == 3)continue;

            List<SelenideElement> elements1 = elements.get(i).$$(By.tagName("td"));

            String currency = elements1.get(0).getText().split("/")[0];

            if(!Arrays.asList(currencies).contains(currency)){
                continue;
            }
            Double buy, sell;

            buy = sell = Double.parseDouble( elements1.get(2).getText());

            ExchangeRates exchangeRates = new ExchangeRates(getDateOfRelevance(elements.get(0)), currency, sell, buy, BANK);
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
