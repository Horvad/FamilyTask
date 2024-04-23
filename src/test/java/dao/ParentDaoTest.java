package dao;

import dao.api.IParentDao;
import dao.ds.DataSourseC3P0;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.AddressEntity;
import dao.entity.ParentEntity;
import dao.fabric.ParentDaoSingleton;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ParentDaoTest {
    static String dbName = "postgres";
    static String login = "postgres";
    static String pass = "123";
    static String schema = "" +
            "CREATE SCHEMA IF NOT EXISTS app" +
            "    AUTHORIZATION postgres;" +
            "CREATE TABLE IF NOT EXISTS app.parent " +
            "(" +
            "    id bigserial NOT NULL, " +
            "    version bigint NOT NULL, " +
            "    name text NOT NULL, " +
            "    id_address bigint NOT NULL, " +
            "    CONSTRAINT parent_pkey PRIMARY KEY (id) " +
            ");" +
            "INSERT INTO app.parent (version, name, id_address) " +
            "VALUES " +
            "(1, 'Иван',1)," +
            "(2, 'Петр',1)," +
            "(3, 'Александр',1)," +
            "(4, 'Стас',2)," +
            "(5, 'Илья',2)," +
            "(6, 'Евгений',1)," +
            "(7, 'Виктор',3)," +
            "(8, 'Алексей',3)," +
            "(9, 'Мирон',3)," +
            "(10, 'Клим',3)";
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName(dbName)
            .withUsername(login)
            .withPassword(pass);
    static IParentDao parentDao;

    @BeforeAll
    static void before() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void after() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() throws PropertyVetoException {
        Properties properties = new Properties();
        properties.put("db.class", postgreSQLContainer.getDriverClassName());
        properties.put("db.url", postgreSQLContainer.getJdbcUrl());
        properties.put("db.login", postgreSQLContainer.getUsername());
        properties.put("db.password", postgreSQLContainer.getPassword());
        IDataSourceWrapper dataSourceWrapper = new DataSourseC3P0(properties);
        parentDao = ParentDaoSingleton.getInstance(dataSourceWrapper);
        try (Connection conn = dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(schema);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void createAndGetAll() {
        List<ParentEntity> parentEntitiesActual = parentDao.getAll();
        parentDao.create(new ParentEntity(null, null, "2", 2L));
        List<ParentEntity> parentEntities = parentDao.getAll();
        Assertions.assertEquals(parentEntitiesActual.size() + 1, parentEntities.size());
        parentEntities.sort(((o1, o2) -> o1.getId().compareTo(o2.getId())));
        ParentEntity parent = parentEntities.get(parentEntities.size() - 1);
        Assertions.assertNotNull(parent.getId());
        Assertions.assertNotNull(parent.getVersion());
        Assertions.assertEquals(parent.getName(), "2");
        Assertions.assertEquals(parent.getAddress(), 2L);
    }

    @Test
    void getId() {
        ParentEntity parent = parentDao.get(1L);
        Assertions.assertEquals(parent.getVersion(), 1L);
        Assertions.assertEquals(parent.getName(), "Иван");
        Assertions.assertNull(parentDao.get(0L));
    }

    @Test
    void update(){
        ParentEntity parent = new ParentEntity(null,null,"2333",1L);
        ParentEntity parentActual = parentDao.get(2L);
        parentDao.update(2L,1L,parent);
        ParentEntity parentUpdate = parentDao.get(2L);
        Assertions.assertEquals(parentActual.getName(),parentUpdate.getName());
        Assertions.assertEquals(parentActual.getAddress(),parentUpdate.getAddress());
        parentDao.update(2L,2L,parent);
        parentUpdate = parentDao.get(2L);
        Assertions.assertEquals(parentUpdate.getName(),parent.getName());
        Assertions.assertEquals(parentUpdate.getAddress(),parent.getAddress());
    }

    @Test
    void delete(){
        List<ParentEntity> parentEntities = parentDao.getAll();
        parentDao.delete(0L,0L);
        List<ParentEntity> parentEntitiesDelete = parentDao.getAll();
        Assertions.assertEquals(parentEntities.size(),parentEntitiesDelete.size());
        parentDao.delete(5L,0L);
        parentEntitiesDelete = parentDao.getAll();
        Assertions.assertEquals(parentEntities.size(),parentEntitiesDelete.size());
        parentDao.delete(5L,5L);
        parentEntitiesDelete = parentDao.getAll();
        Assertions.assertEquals(parentEntities.size()-1,parentEntitiesDelete.size());
        Assertions.assertNull(parentDao.get(5L));
    }

    @Test
    void exist(){
        Assertions.assertFalse(parentDao.exist(0L));
        Assertions.assertTrue(parentDao.exist(6L));
    }
}
