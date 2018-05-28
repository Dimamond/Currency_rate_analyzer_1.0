import dao.ExchangeRatesDao;
import model.ExchangeRates;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();

        ExchangeRates exchangeRates = new ExchangeRates(new Date(), "USD", 30.30, 31.20, "СБЕРБАНК");
        exchangeRatesDao.insertExchangeRates(exchangeRates);


    }
}
