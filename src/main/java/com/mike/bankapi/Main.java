package com.mike.bankapi;

import com.mike.bankapi.service.ClientService;
import com.mike.bankapi.model.dao.*;
import com.mike.bankapi.service.HttpWebServer;
import com.mike.bankapi.service.ServiceException;
import com.mike.bankapi.service.Utils;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DAOFactory daoFactory = null;
        try {
            daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
            daoFactory.initDb();
        } catch (DAOException e) {
            Utils.printMessage("Ошибка dao-layer");
            e.printStackTrace();
        }

        ClientService clientService = new ClientService(daoFactory);

        HttpWebServer httpWebServer = null;
        try {
            httpWebServer = new HttpWebServer(clientService);
            httpWebServer.start();
        } catch (ServiceException e) {
            Utils.printMessage("Ошибка service-layer");
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        Utils.printMessage("Введите 'exit' для остановки сервера и выхода из программы");
        String input;
        do {
            input = scanner.nextLine();
        }
        while (!input.equalsIgnoreCase("exit"));

        Utils.printMessage("Останавливаем сервер, пожалуйста подождите");

        daoFactory.closeAllConnections();
        if (httpWebServer != null) {
            httpWebServer.stop();
        }

    }
}
