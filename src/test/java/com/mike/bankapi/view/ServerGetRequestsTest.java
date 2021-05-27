package com.mike.bankapi.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.service.HttpWebServer;
import com.mike.bankapi.service.ServiceException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ServerGetRequestsTest {
    public static String urlMain = "http://" + HttpWebServer.SERVER_HOSTNAME + ":" + HttpWebServer.SERVER_PORT;
    public static String urlCards = urlMain + "/clients/cards/";
    public static String urlBalance = urlMain + "/clients/balance/";

    private static ClientController clientController;
    private static ObjectMapper mapper;
    private static HttpWebServer httpWebServer;

    @BeforeClass
    public static void mainInit() throws DAOException, ServiceException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();

        clientController = new ClientController(daoFactory);

        httpWebServer = new HttpWebServer(clientController);
        httpWebServer.start();

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterClass
    public static void after() {
        httpWebServer.stop();
    }

    @Test
    public void getCardsList() throws IOException, DAOException {
        String url = urlCards + "?client_id=1";
        JsonNode actual = actualResponse(url);

        String expectedString = "[{\"cardId\":1,\"accountId\":1,\"cardNumber\":\"5419706295307412\",\"dailyLimit\":0.0}]";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCardsListFailOnClientId() throws IOException, DAOException {
        String url = urlCards + "?client_id=20";
        JsonNode actual = actualResponse(url);

        String expectedString = "{\"error\":\"Ошибка! Не удалось получить клиента по Id\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCardsListFailOnUrl() throws IOException, DAOException {
        String url = urlCards + "3232123dsd";
        JsonNode actual = actualResponse(url);

        String expectedString = "{\"error\":\"Ошибка! Неверные входные данные в запросе\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getBalance() throws IOException, DAOException {
        String url = urlBalance + "?account_id=3";
        JsonNode actual = actualResponse(url);

        String expectedString = "{\"accountId\":3,\"clientId\":2,\"number\":\"40261494800000008428\",\"balance\":39076.09,\"cardList\":null}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getBalanceFailOnAccountId() throws IOException, DAOException {
        String url = urlBalance + "?account_id=20";
        JsonNode actual = actualResponse(url);

        String expectedString = "{\"error\":\"Ошибка! Не удалось получить счет по Id\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getBalanceFailOnUrl() throws IOException, DAOException {
        String url = urlBalance + "?account";
        JsonNode actual = actualResponse(url);

        String expectedString = "{\"error\":\"Ошибка! Неверные входные данные в запросе\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    private JsonNode actualResponse(String urlToGet) throws IOException {
        URL url = new URL(urlToGet);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(false);

        JsonNode jsonNode = mapper.readTree(connection.getInputStream());

        return jsonNode;
    }
}