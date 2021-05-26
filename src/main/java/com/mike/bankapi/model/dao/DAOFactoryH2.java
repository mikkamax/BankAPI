package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.dao.h2.AccountDAOH2;
import com.mike.bankapi.model.dao.h2.CardDAOH2;
import com.mike.bankapi.model.dao.h2.ClientDAOH2;
import com.mike.bankapi.service.Utils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOFactoryH2 extends DAOFactory {
    private static final String DB_URL = "jdbc:h2:mem:bankapi;DB_CLOSE_DELAY=-1";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final String INIT_DB_FILE = "initDb.sql";

    private JdbcConnectionPool jdbcConPool;
    private Server tcpServer;

    public DAOFactoryH2() throws DAOException {
        jdbcConPool = JdbcConnectionPool.create(DB_URL, DB_USERNAME, DB_PASSWORD);
        //тестовый сервер для доступа к базе
        startTcpServer();
    }

    @Override
    public Connection getConnection() throws DAOException {
        try {
            return jdbcConPool.getConnection();
        } catch (SQLException e) {
            Utils.printMessage("Не удалось получить коннекшн к БД из коннекшн пула");
            throw new DAOException("Не удалось получить коннекшн к БД из коннекшн пула", e);
        }
    }

    @Override
    public void closeAllConnections() {
        jdbcConPool.dispose();
        Utils.printMessage("Соединения к БД закрыты");
        if (tcpServer != null) {
            tcpServer.stop();
            Utils.printMessage("TCP сервер для доступа к базе остановлен");
        }
    }

    @Override
    public ClientDAO getClientDAO() {
        return new ClientDAOH2(this);
    }

    @Override
    public AccountDAO getAccountDAO() {
        return new AccountDAOH2(this);
    }

    @Override
    public CardDAO getCardDAO() {
        return new CardDAOH2(this);
    }

    public void initDb() throws DAOException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String initDbSql = Utils.readFileToString(INIT_DB_FILE);
            statement.execute(initDbSql);
            Utils.printMessage("База данных с тестовыми данными создана.");
        } catch (SQLException e) {
            Utils.printMessage("Проблема с созданием и заполнением БД");
            throw new DAOException("Проблема с созданием и заполнением БД", e);
        }
    }

    public void startTcpServer() throws DAOException {
        try {
            tcpServer = Server.createTcpServer();
            tcpServer.start();
            Utils.printMessage("Запущен tcp сервер для доступа к БД.");
        } catch (SQLException e) {
            Utils.printMessage("Ошибка при запуске tcp сервера для доступа к БД");
            throw new DAOException("Ошибка при запуске tcp сервера для доступа к БД", e);
        }
    }
}
