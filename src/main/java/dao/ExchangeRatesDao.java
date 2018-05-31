package dao;


import model.ExchangeRates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Date;

public class ExchangeRatesDao {
    private final static Logger LOG = LogManager.getLogger(ExchangeRatesDao.class.getName());
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage());
        }
    }

    private Connection getConnection()throws SQLException{
        return DriverManager.getConnection("jdbc:mariadb://localhost:3307/exchange_rates_db", "root", "root");
    }

    private void closeConnection(Connection connection){
        if(connection == null)return;
        try {
            connection.close();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

    }



    public Long selectBankIdByBankName(String bankName){
        Long bankId = null;
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM banks WHERE name = ?");
            pst.setString(1, bankName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                bankId = rs.getLong("id");
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return bankId;
    }

    public Long selectCurrenciesIdByCurrenciesName(String currenciesName){
        Long currenciesId = null;
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM currencies WHERE name = ?");
            pst.setString(1, currenciesName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                currenciesId = rs.getLong("id");
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return currenciesId;

    }

    public void insertExchangeRates(ExchangeRates exchangeRates){
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO" +
                    " exchange_rates (date_parsing, date_of_relevance, currencies_id, sell, buy, bank_id)" +
                    " VALUES (?, ?, ?, ?, ?, ?)");


            if(exchangeRates.getDateParsing() != null){
                pst.setTimestamp(1, new Timestamp(exchangeRates.getDateParsing().getTime()));
            }else pst.setTimestamp(1, null);

            if(exchangeRates.getDateOfRelevance() != null){
                pst.setTimestamp(2, new Timestamp(exchangeRates.getDateOfRelevance().getTime()));
            }else pst.setTimestamp(2, null);

            pst.setLong(3, selectCurrenciesIdByCurrenciesName(exchangeRates.getCurrencies()));
            pst.setDouble(4, exchangeRates.getSell());
            pst.setDouble(5, exchangeRates.getBuy());
            pst.setLong(6, selectBankIdByBankName(exchangeRates.getBank()));
            pst.execute();

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }finally {
            closeConnection(connection);
        }


    }



}
