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

/**
 * ДАО фабрика для работы с базой данных H2
 */

public class DAOFactoryH2 extends DAOFactory {
    //константы для базы данных
    private static final String DB_URL = "jdbc:h2:mem:bankapi;DB_CLOSE_DELAY=-1";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final String INIT_DB_FILE = "/Users/u19208062/IdeaProjects/BankAPI/src/main/resources/initDb.sql";

    /**
     * Реализация ConnectionPool от создателей БД H2
     */
    private JdbcConnectionPool jdbcConPool;

    /**
     * В случае необходимости получить доступ к БД, созданной в памяти, использцуется реализация tcpServer от H2
     */
    private Server tcpServer;

    /**
     * В конструкторе создается jdbcConnectionPool
     * Также для тестов с БД можно раскомментировать старт TCP-сервера
     */
    public DAOFactoryH2() {
        jdbcConPool = JdbcConnectionPool.create(DB_URL, DB_USERNAME, DB_PASSWORD);
        //тестовый сервер для доступа к базе
        //startTcpServer();
    }

    /**
     * Получаем соединение из коннекшн пула
     * @return Connection - соединение к БД
     * @throws DAOException если не удалось получить коннекшн
     */
    @Override
    public Connection getConnection() throws DAOException {
        try {
            return jdbcConPool.getConnection();
        } catch (SQLException e) {
            String error = "Не удалось получить коннекшн к БД из коннекшн пула";
            Utils.printMessage(error);
            throw new DAOException(error, e);
        }
    }

    /**
     * Закрываем все соединения в коннекшн пуле и останавливаем tcpServer, если он был запущен
     */
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

    /**
     * Инициализация БД с созданием таблиц и внесением тестовых данных
     * @throws DAOException если не получилось создать или заполнить БД
     */
    public void initDb() throws DAOException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String initDbSql = Utils.readFileToString(INIT_DB_FILE);
            statement.execute(initDbSql);
            Utils.printMessage("База данных с тестовыми данными создана.");
        } catch (SQLException e) {
            String error = "Проблема с созданием и заполнением БД";
            Utils.printMessage(error);
            throw new DAOException(error, e);
        }
    }

    /**
     * Стартует TCP-server от H2, позволяющий получить доступ к администрировнию БД, находящейся в памяти, в момент исполнения программы
     * @throws DAOException при ошибке запуска tcp сервера"
     */
    public void startTcpServer() throws DAOException {
        try {
            tcpServer = Server.createTcpServer();
            tcpServer.start();
            Utils.printMessage("Запущен tcp сервер для доступа к БД.");
        } catch (SQLException e) {
            String error = "Ошибка при запуске tcp сервера для доступа к БД";
            Utils.printMessage(error);
            throw new DAOException(error, e);
        }
    }
}
