package dao;


import model.ExchangeRates;

import java.sql.*;

public class ExchangeRatesDao {
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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

            pst.setTimestamp(1, new Timestamp(exchangeRates.getDateParsing().getTime()));
            pst.setTimestamp(2, new Timestamp(exchangeRates.getDateOfRelevance().getTime()));
            pst.setLong(3, selectCurrenciesIdByCurrenciesName(exchangeRates.getCurrencies()));
            pst.setDouble(4, exchangeRates.getSell());
            pst.setDouble(5, exchangeRates.getBuy());
            pst.setLong(6, selectBankIdByBankName(exchangeRates.getBank()));
            pst.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(connection);
        }


    }



}
