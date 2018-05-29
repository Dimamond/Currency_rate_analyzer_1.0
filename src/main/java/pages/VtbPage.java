package pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import model.ExchangeRates;
import org.openqa.selenium.By;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;


public class VtbPage implements Page {

    private final static String BANK = "ВТБ";

    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.vtb.ru/personal/platezhi-i-perevody/obmen-valjuty/");
    }


    private void openPageWithExchangeRates(){
        String link = $("div.tab-panel__tab-content_padding-40x40").$(By.tagName("iframe")).attr("src");
        open(link);
    }

    private Date getDateOfRelevance(){

        String dateTemp = $(By.className("mt36")).getText().substring(32, 48);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Date dateOfRelevance = null;
        try {
            dateOfRelevance = dateFormat.parse(dateTemp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOfRelevance;
    }

    private ExchangeRates getExchangeRates(SelenideElement se){
        ExchangeRates exchangeRates = null;
        String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY", "CNY"};

        List<SelenideElement> elements = se.$$(By.tagName("td"));

        String currency = elements.get(0).getText().split("\n")[0].trim();

        if(!Arrays.asList(currencies).contains(currency)){
            return null;
        }

        int unit = 1;
        try {
            unit = Integer.parseInt(elements.get(1).getText().split(" ")[0]);
        } catch (NumberFormatException e) {

        }

        Double buy = Double.parseDouble(elements.get(2).getText().replace(",", "."));

        Double sell = Double.parseDouble(elements.get(3).getText().replace(",","."));

        exchangeRates = new ExchangeRates(getDateOfRelevance(), currency, sell/unit, buy/unit, BANK);


        return exchangeRates;
    }

    private List<ExchangeRates> getInfoAboutCurrencies(){
        List<ExchangeRates> list = new ArrayList<>();

        List<SelenideElement> elements = $(By.tagName("tbody")).$$(By.tagName("tr"));

        for (SelenideElement se : elements){
            ExchangeRates exchangeRates = getExchangeRates(se);
            if(exchangeRates != null)
                list.add(exchangeRates);
        }

        return list;
    }

    public List<ExchangeRates> getListExchangeRates(){

        initialization();
        openPageWithExchangeRates();
        return getInfoAboutCurrencies();
    }



}
