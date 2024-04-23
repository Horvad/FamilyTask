package dao.ds.fabric;

import dao.ds.DataSourseC3P0;
import dao.ds.api.IDataSourceWrapper;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class DataSourceSingleton {
    private static volatile IDataSourceWrapper instance;
    private static Properties properties;

    private DataSourceSingleton(){};

    public static IDataSourceWrapper getInstance() throws PropertyVetoException {
        if(instance==null){
            synchronized (DataSourceSingleton.class){
                if(instance==null){
                    instance = new DataSourseC3P0(properties);
                }
            }
        }
        return instance;
    }

    public static void setProperties(Properties properties) {
        synchronized (DataSourceSingleton.class){
            if(instance == null){
                DataSourceSingleton.properties = properties;
            }
        }
    }
}
