package pages;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import model.ExchangeRates;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.codeborne.selenide.Selenide.*;


public class SberbankPage implements Page {
    private final static String BANK = "СБЕРБАНК";


    private void initialization(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("https://www.sberbank.ru/ru/quotes/currencies");
    }

    private void currencySelectionOnPage(String currency) {

        SelenideElement element = null;
        element = $(By.cssSelector("div.select"));
        element.click();
        List<SelenideElement> elements = element.$$(By.tagName("span"));

        for (SelenideElement se : elements){
            if(se.getText().equalsIgnoreCase(currency.trim())){
                se.click();
                break;
            }
        }
    }

    private void selectedCheckBoxes(){
        List<SelenideElement> elements = $(By.cssSelector("div.kit-checkbox-group")).$$(By.tagName("label"));
        for (SelenideElement se : elements){
            String isSelected = se.getAttribute("aria-checked");
            if(isSelected.equals("false"))se.click();
        }
    }

    public Date getDateOfRelevance(){
        String dateTemp = $(By.cssSelector("div.rates-current__info")).getText().substring(18, 34).trim();

        SimpleDateFormat dateFormat;

        if(dateTemp.length() == 15)
            dateFormat = new SimpleDateFormat("dd.MM.yyyy H:mm");
        else
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        Date dateOfRelevance = null;
        try {
            dateOfRelevance = dateFormat.parse(dateTemp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOfRelevance;
    }

    private List<ExchangeRates> getInfoAboutCurrencies(){

        List<ExchangeRates> list = new ArrayList<>();

        List<SelenideElement> elements = $(By.cssSelector("table.rates-current__table")).$$(By.tagName("tr"));

        for (int i = 1; i < elements.size(); i++){
            SelenideElement se = elements.get(i);

            String currency = se.$(By.cssSelector("td.rates-current__table-cell_column_name")).getText();
            String[] temp = currency.split("/");
            currency = temp[0].trim();

            String temp0 = se.$(By.cssSelector("td.rates-current__table-cell_column_scale")).getText();
            int unit = Integer.parseInt(temp0);

            String temp1 = se.$(By.cssSelector("td.rates-current__table-cell_column_buy")).$(By.className("rates-current__rate-value")).getText();
            Double buy = Double.parseDouble(temp1.replace(",", "."));

            temp1 = se.$(By.cssSelector("td.rates-current__table-cell_column_sell")).$(By.className("rates-current__rate-value")).getText();
            Double sell = Double.parseDouble(temp1.replace(",", "."));

            ExchangeRates exchangeRates = new ExchangeRates(getDateOfRelevance(), currency, sell/unit, buy/unit, BANK);
            list.add(exchangeRates);

        }

        return list;
    }

    public List<ExchangeRates> getListExchangeRates(){
        String[] currencies = {"Доллар США", "Евро", "Фунт стерлингов Соединенного Королевства",
                "Швейцарский франк", "Китайский Юань Жэньминьби", "Японская иена"};

        initialization();

        for (int i = 0; i < currencies.length; i++){
            currencySelectionOnPage(currencies[i]);
        }

        selectedCheckBoxes();

        return getInfoAboutCurrencies();
    }


}
