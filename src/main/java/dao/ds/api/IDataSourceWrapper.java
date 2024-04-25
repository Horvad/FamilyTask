package dao.ds.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс предназначен для получения и закрытия Connection  баз данных на слоях dao
 */
public interface IDataSourceWrapper extends AutoCloseable{
    /**
     * Полечение Connection
     * @return
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;
}
