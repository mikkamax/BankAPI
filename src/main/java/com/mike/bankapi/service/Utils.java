package com.mike.bankapi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Utils {
    public static String readFileToString(String fileName) {
        StringBuilder sb = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            printMessage("Ошибка! Файл для загрузки не найден");
        }

        return sb.toString();
    }

    public static String generateNewNumber(int length) {
        Random rnd = new Random();
        String input = "1234567890";
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(input.charAt(rnd.nextInt(10)));
        }

        return sb.toString();
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
