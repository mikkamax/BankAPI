package com.mike.bankapi.service;

import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.view.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpWebServer {
    private final static String SERVER_HOSTNAME = "localhost";
    private final static int SERVER_PORT = 8090;
    private final static int SERVER_QUEUE = 0;

    private ClientController clientController;
    private HttpServer httpServer;

    public HttpWebServer(ClientController clientController) throws ServiceException {
        this.clientController = clientController;
        init();
    }

    public void init() throws ServiceException {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), SERVER_QUEUE);
            httpServer.createContext("/clients/testapi/", new ClientsTestAPIHandler(clientController));
            httpServer.createContext("/clients/cards/", new ClientsCardsHandler(clientController));
            httpServer.createContext("/clients/balance/", new ClientsBalanceHandler(clientController));
            httpServer.createContext("/clients/new_card/", new ClientsNewCardHandler(clientController));
            httpServer.createContext("/clients/add_funds/", new ClientsAddFundsHandler(clientController));
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
//        httpServer.setExecutor(threadPoolExecutor);
        } catch (IOException e) {
            String error = "Ошибка инициализации веб-сервера";
            Utils.printMessage(error);
            throw new ServiceException(error, e);
        }

    }

    public void start() {
        httpServer.start();
        Utils.printMessage("HttpServer started on port " + SERVER_PORT);
    }

    public void stop() {
        httpServer.stop(0);
        Utils.printMessage("HttpServer stopped on port " + SERVER_PORT);
    }
}
