package service;

import dao.ExchangeRatesDao;
import model.ExchangeRates;
import pages.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import static com.codeborne.selenide.Selenide.*;

public class Service extends TimerTask {
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
            exchangeRates.addAll(p.getListExchangeRates());
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
