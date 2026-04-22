package ru.p0keta.Yandex.Market;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения файла config.properties
 * Содержит метод для доступа к параметрам из файла.
 */
public final class ConfigReader {
    private static final Properties properties = new Properties();

    private ConfigReader() {}

    static {
        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else  {
                throw new FileNotFoundException("config.properties не найден в resources");
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    /**
     * Метод для чтения из файла текстовых значений
     *
     * @param key ключ в файле
     * @return значение согласно ключу
     */
    public static String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Метод для чтения из файла целочисленных значений
     *
     * @param key ключ в файле
     * @return значение согласно ключу
     */
    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}