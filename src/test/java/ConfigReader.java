import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Класс для чтения файла config.properties
 * Содержит метод для доступа к параметрам из файла.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}