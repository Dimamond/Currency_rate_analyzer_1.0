package pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import dao.ExchangeRatesDao;
import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;


public class RSHBPage implements Page {
    private final static Logger LOG = LogManager.getLogger(RSHBPage.class.getName());
    private final static String BANK = "РоссельхозБанк";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.rshb.ru/branches/moscow/currency-rates/");
    }

    private Date getDateOfRelevance(){

        String dateTemp = $(By.cssSelector("div.b-rates-title")).getText().replace("с", "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
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

        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(0, "USD");
        map.put(2, "EUR");
        map.put(4, "GBP");
        map.put(5, "CHF");
        map.put(6, "CNY");


        List<SelenideElement> elements = $(By.className("full")).$$(By.tagName("tr")).get(1).$$(By.tagName("td"));

        List<SelenideElement> listBuy = elements.get(1).$$(By.tagName("p"));
        List<SelenideElement> listSell = elements.get(2).$$(By.tagName("p"));


        for (Map.Entry<Integer, String> entry : map.entrySet()){
            listBuy.get(entry.getKey());

            Double buy = Double.parseDouble(listBuy.get(entry.getKey()).getText());

            Double sell = Double.parseDouble(listSell.get(entry.getKey()).getText());

            int unit = 1;
            if(entry.getValue().equals("CNY")){
                unit = 10;
            }

            ExchangeRates exchangeRates = new ExchangeRates(getDateOfRelevance(), entry.getValue(), sell/unit, buy/unit, BANK);
            list.add(exchangeRates);

        }

        return list;
    }


    public List<ExchangeRates> getListExchangeRates(){
        initialization();
        return getInfoAboutCurrencies();
    }

}
