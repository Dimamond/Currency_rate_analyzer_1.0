package service;

import dao.ExchangeRatesDao;
import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.Page;


import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import static com.codeborne.selenide.Selenide.*;

public class Service extends TimerTask {
    private final static Logger LOG = LogManager.getLogger(Service.class.getName());
    private List<Page> pages;
    private ExchangeRatesDao dao;

    public Service(List<Page> pages, ExchangeRatesDao dao) {
        this.pages = pages;
        this.dao = dao;
    }

    public List<Page> getPages() {
        return pages;
    }

    public ExchangeRatesDao getDao() {
        return dao;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public void setDao(ExchangeRatesDao dao) {
        this.dao = dao;
    }

    public void saveDataToDB(){
        List<ExchangeRates> exchangeRates = new ArrayList<>();

        for (Page p : pages){

            try {
                exchangeRates.addAll(p.getListExchangeRates());
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

        for (ExchangeRates er : exchangeRates){
            dao.insertExchangeRates(er);
        }
    }

    @Override
    public void run() {
        saveDataToDB();
        close();
    }
}
