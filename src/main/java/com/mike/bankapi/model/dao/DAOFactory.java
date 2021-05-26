package com.mike.bankapi.model.dao;

import com.mike.bankapi.service.Utils;

import java.sql.Connection;

public abstract class DAOFactory {
    //поддерживаемые бд
    public static final int H2_DB = 1;

    //получение connection реализует конкретная фабрика
    public abstract Connection getConnection() throws DAOException;
    public abstract void closeAllConnections();

    //инициализация базы при необходимости реализует конкретная фабрика
    public abstract void initDb() throws DAOException;

    //эти методы реализуют конкретные имплементации фабрики
    public abstract ClientDAO getClientDAO();
    public abstract AccountDAO getAccountDAO();
    public abstract CardDAO getCardDAO();

    public static DAOFactory getDAOFactory(int factoryType) throws DAOException {
        switch (factoryType) {
            case H2_DB:
                return new DAOFactoryH2();
            default:
                Utils.printMessage("Фабрика " + factoryType + " не существует");
                return null;
        }
    }
}
