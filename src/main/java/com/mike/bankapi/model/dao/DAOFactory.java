package com.mike.bankapi.model.dao;

import com.mike.bankapi.service.Utils;

import java.sql.Connection;

/**
 * Абстрактная фабрика, статический метод getDAOFactory возвращает конкретную реализацию фабрики
 */

public abstract class DAOFactory {
    //поддерживаемые бд
    public static final int H2_DB = 1;

    /**
     * Конкретная реализация фабрики открывает соединение к БД
     * @return Connection - соединения к БД
     * @throws DAOException если получить соединение не удалось
     */
    public abstract Connection getConnection() throws DAOException;

    /**
     * Закрываем все соединения к базе данных
     */
    public abstract void closeAllConnections();

    /**
     * Инициализация базы данных... реализует конкретная фабрика (при необходимости)
     * @throws DAOException если возникли ошибки при инициализации БД
     */
    public abstract void initDb() throws DAOException;

    //эти методы реализуют конкретные имплементации фабрики
    public abstract ClientDAO getClientDAO();
    public abstract AccountDAO getAccountDAO();
    public abstract CardDAO getCardDAO();

    /**
     * Возвращает конкретную имплементацию фабрики
     * @param factoryType - одна из поддерживаемых БД (константа)
     * @return конкретная реализация DAOFactory
     * @throws DAOException если произошла ошибка при создании фабрики, или если в метод подан неверный factoryType
     */
    public static DAOFactory getDAOFactory(int factoryType) throws DAOException {
        switch (factoryType) {
            case H2_DB:
                return new DAOFactoryH2();
            default:
                String error = "Фабрика " + factoryType + " не существует";
                Utils.printMessage(error);
                throw new DAOException(error);
        }
    }
}
