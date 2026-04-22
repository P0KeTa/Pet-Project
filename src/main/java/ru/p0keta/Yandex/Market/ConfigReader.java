package ru.p0keta.Yandex.Market;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения файла config.properties
 * Содержит метод для доступа к параметрам из файла.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}