package com.mike.bankapi.service;

import com.mike.bankapi.controllers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Занимается инициализацией, запуском и остановкой HTTP-Server
 */
public class HttpWebServer {
    public final static String SERVER_HOSTNAME = "localhost";
    public final static int SERVER_PORT = 8090;
    public final static int SERVER_QUEUE = 0;

    private ClientService clientService;
    private HttpServer httpServer;

    /**
     * Получает контроллер и инициализирует веб-сервер
     * @param clientService - контроллер, отвечающий за обработку запросов из Handler'ов
     * @throws ServiceException
     */
    public HttpWebServer(ClientService clientService) throws ServiceException {
        this.clientService = clientService;
        init();
    }

    /**
     * Инициализация веб-сервера
     *
     * @throws ServiceException
     */
    public void init() throws ServiceException {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), SERVER_QUEUE);
            httpServer.createContext("/clients/testapi/", new ClientsTestAPIHandler(clientService));
            httpServer.createContext("/clients/", new ClientsHandler(clientService));
            httpServer.createContext("/clients/cards/", new ClientsCardsHandler(clientService));
            httpServer.createContext("/clients/balance/", new ClientsBalanceHandler(clientService));
            httpServer.createContext("/clients/new_card/", new ClientsNewCardHandler(clientService));
            httpServer.createContext("/clients/add_funds/", new ClientsAddFundsHandler(clientService));
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
//        httpServer.setExecutor(threadPoolExecutor);
        } catch (IOException e) {
            String error = "Ошибка инициализации веб-сервера";
            Utils.printMessage(error);
            throw new ServiceException(error, e);
        }

    }

    /**
     * Запуск сервера
     */
    public void start() {
        httpServer.start();
        Utils.printMessage("HttpServer started on port " + SERVER_PORT);
    }

    /**
     * Остановка сервера
     */
    public void stop() {
        httpServer.stop(0);
        Utils.printMessage("HttpServer stopped on port " + SERVER_PORT);
    }
}
