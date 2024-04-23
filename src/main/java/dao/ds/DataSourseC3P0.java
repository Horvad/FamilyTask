package dao.ds;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dao.ds.api.IDataSourceWrapper;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourseC3P0 implements IDataSourceWrapper {
    private static final String DRIVER_CLASS_PROPERTY_NAME = "db.class";
    private static final String URL_PROPERTY_NAME = "db.url";
    private static final String USER_PROPERTY_NAME = "db.login";
    private static final String PASSWORD_PROPERTY_NAME = "db.password";

    private ComboPooledDataSource cpds;

    public DataSourseC3P0(Properties properties) throws PropertyVetoException {
        this.cpds = new ComboPooledDataSource();
        this.cpds.setDriverClass(properties.getProperty(DRIVER_CLASS_PROPERTY_NAME));
        this.cpds.setJdbcUrl(properties.getProperty(URL_PROPERTY_NAME));
        this.cpds.setUser(properties.getProperty(USER_PROPERTY_NAME));
        this.cpds.setPassword(properties.getProperty(PASSWORD_PROPERTY_NAME));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

    @Override
    public void close() throws Exception {
        this.cpds.close();
    }
}
