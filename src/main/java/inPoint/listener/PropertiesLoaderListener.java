package inPoint.listener;

import dao.ds.api.IDataSourceWrapper;
import dao.ds.fabric.DataSourceSingleton;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.*;
import java.util.Properties;

/**
 * Класс для чения properties при инициализации программы
 */
public class PropertiesLoaderListener implements ServletContextListener {
    /**
     * Чтение property из папки conf tomcat
     * @param sce the ServletContextEvent containing the ServletContext that is being initialized
     *
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try{
            File confDir = new File(System.getenv("catalina_base") + "/conf");
            File prop = new File(confDir + "/application.properties");
            Properties properties = new Properties();
            properties.load(new FileReader(prop));

            DataSourceSingleton.setProperties(properties);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Файл с настройками не найден!", e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла настроек прилажения application.properties", e);
        }
    }

    /**
     * Закрытие connect DS при закрытии приложения
     * @param sce the ServletContextEvent containing the ServletContext that is being destroyed
     *
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            IDataSourceWrapper dataSourceWrapper = DataSourceSingleton.getInstance();
            dataSourceWrapper.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}