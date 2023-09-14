package util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {}

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
