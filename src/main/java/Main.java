import dao.ExchangeRatesDao;
import model.ExchangeRates;
import pages.SberbankPage;

import java.util.Date;

public class Main {
    public static void main(String[] args) {


        ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();
        SberbankPage sberbankPage = new SberbankPage();
        sberbankPage.getListExchangeRates();

        for (ExchangeRates exchangeRates: sberbankPage.getListExchangeRates()){
            exchangeRatesDao.insertExchangeRates(exchangeRates);

        }

    }
}
