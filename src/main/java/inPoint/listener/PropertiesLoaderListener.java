package inPoint.listener;

import dao.ds.api.IDataSourceWrapper;
import dao.ds.fabric.DataSourceSingleton;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.*;
import java.util.Properties;

public class PropertiesLoaderListener implements ServletContextListener {
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