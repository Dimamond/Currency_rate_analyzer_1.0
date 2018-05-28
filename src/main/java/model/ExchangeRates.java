package model;

import java.util.Date;

public class ExchangeRates {
    private Date dateParsing;
    private Date dateOfRelevance;
    private String currencies;
    private Double sell;
    private Double buy;
    private String bank;

    public ExchangeRates(Date dateOfRelevance, String currencies, Double sell, Double buy, String bank) {
        this.dateParsing = new Date();
        this.dateOfRelevance = dateOfRelevance;
        this.currencies = currencies;
        this.sell = sell;
        this.buy = buy;
        this.bank = bank;
    }

    public Date getDateParsing() {
        return dateParsing;
    }

    public Date getDateOfRelevance() {
        return dateOfRelevance;
    }

    public String getCurrencies() {
        return currencies;
    }

    public Double getSell() {
        return sell;
    }

    public Double getBuy() {
        return buy;
    }

    public String getBank() {
        return bank;
    }

    public void setDateParsing(Date dateParsing) {
        this.dateParsing = dateParsing;
    }

    public void setDateOfRelevance(Date dateOfRelevance) {
        this.dateOfRelevance = dateOfRelevance;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
