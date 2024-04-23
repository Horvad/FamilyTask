package dao;

import dao.api.IChildrenDao;
import dao.ds.DataSourseC3P0;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.ChildrenEntity;
import dao.entity.ParentEntity;
import dao.fabric.ChildrenDaoSingleton;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;
import org.testcontainers.containers.PostgreSQLContainer;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChildrenDaoTest {
    static String dbName = "postgres";
    static String login = "postgres";
    static String pass = "123";

    static String sccc =             "CREATE SCHEMA IF NOT EXISTS app" +
            "    AUTHORIZATION postgres;" +
            "CREATE TABLE IF NOT EXISTS app.address " +
            "( " +
            "       id bigserial NOT NULL, " +
            "       version bigint NOT NULL, " +
            "       street text NOT NULL, " +
            "       number_house integer NOT NULL, " +
            "       number_flat integer NOT NULL, " +
            "       CONSTRAINT address_pkey PRIMARY KEY (id)" +
            ");" +
            "CREATE TABLE IF NOT EXISTS app.parent " +
            "(" +
            "    id bigserial NOT NULL, " +
            "    version bigint NOT NULL, " +
            "    name text NOT NULL, " +
            "    id_address bigint NOT NULL, " +
            "    CONSTRAINT parent_pkey PRIMARY KEY (id), " +
            "        CONSTRAINT parent_id_address_fkey FOREIGN KEY (id_address) " +
            "            REFERENCES app.address (id) MATCH SIMPLE " +
            "            ON UPDATE NO ACTION " +
            "            ON DELETE NO ACTION " +
            ");" +
            "CREATE TABLE IF NOT EXISTS app.children " +
            "(" +
            "    id bigserial NOT NULL, " +
            "    version bigint NOT NULL, " +
            "    name text NOT NULL, " +
            "    CONSTRAINT children_pkey PRIMARY KEY (id) " +
            "); " +
            "CREATE TABLE IF NOT EXISTS app.children_parent_cross " +
            "(" +
            "    id_children bigint, " +
            "    id_parent bigint, " +
            "    CONSTRAINT children_parent_cross_id_children_fkey FOREIGN KEY (id_children) " +
            "        REFERENCES app.children (id) MATCH SIMPLE " +
            "        ON UPDATE NO ACTION " +
            "        ON DELETE NO ACTION, " +
            "    CONSTRAINT children_parent_cross_id_parent_fkey FOREIGN KEY (id_parent) " +
            "        REFERENCES app.parent (id) MATCH SIMPLE " +
            "        ON UPDATE NO ACTION " +
            "        ON DELETE NO ACTION " +
            ");" +
            "INSERT INTO app.address (version, street, number_house, number_flat) " +
            "VALUES " +
            "(1,'Street1',1,11)," +
            "(2,'Street2',2,22)," +
            "(3,'Streert',3,33);" +
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
            "(10, 'Клим',3);" +
            "INSERT INTO app.children (version, name) " +
            "VALUES " +
            "(1, 'One'), " +
            "(2, 'Two'), " +
            "(3, 'Tree'), " +
            "(4, 'Four'), " +
            "(5, 'Five'), " +
            "(6, 'Six'), " +
            "(7, 'Seven'), " +
            "(8, 'Eight'), " +
            "(9, 'Nine'), " +
            "(10, 'Ten'), " +
            "(11, 'Eleven'), " +
            "(12, 'Twelve'),  "+
            "(13, 'Thirteen'), " +
            "(14, 'Fourteen'), " +
            "(15, 'Fifteen'); " +
            "INSERT INTO app.children_parent_cross (id_children, id_parent) " +
            "VALUES " +
            "(1, 1)," +
            "(2, 1), " +
            "(3, 1), " +
            "(3, 2), " +
            "(3, 1), " +
            "(4, 4), " +
            "(5, 4), " +
            "(6, 4), " +
            "(7, 4), " +
            "(8, 5) , " +
            "(9, 5), " +
            "(10, 6), " +
            "(11, 1), " +
            "(12, 2), " +
            "(13, 3), " +
            "(14, 4), " +
            "(15, 5);";

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName(dbName)
            .withUsername(login)
            .withPassword(pass);
    static IChildrenDao childrenDao;

    static IDataSourceWrapper dataSourceWrapper;

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
        dataSourceWrapper = new DataSourseC3P0(properties);
        childrenDao = ChildrenDaoSingleton.getInstance(dataSourceWrapper);
//        try (Connection conn = dataSourceWrapper.getConnection();
//             PreparedStatement preparedStatement = conn.prepareStatement(sccc);) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Test
    void getAll() {
        List<ChildrenEntity> childrenEntities = childrenDao.getAll();
        List<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        ChildrenEntity children = new ChildrenEntity(null,null,"16",longs);
        childrenDao.create(children);
        Assertions.assertEquals(childrenEntities.size()+1,childrenDao.getAll().size());
        childrenEntities = childrenDao.getAll();
        Assertions.assertEquals(children.getName(),childrenEntities.get(childrenEntities.size()-1).getName());
        Assertions.assertArrayEquals(children.getIdParents().toArray(),
                childrenEntities.get(childrenEntities.size()-1).getIdParents().toArray());
        longs.add(20L);
        Assertions.assertThrows(RuntimeException.class,
                ()->childrenDao.create(new ChildrenEntity(null,null,"16",longs)));

    }

    @Test
    void getId() {
        ChildrenEntity children = childrenDao.get(1L);
        Assertions.assertEquals(children.getVersion(), 1L);
        Assertions.assertEquals(children.getName(), "One");
        List<Long> longs = new ArrayList<>();
        longs.add(1L);
        Assertions.assertArrayEquals(children.getIdParents().toArray(), longs.toArray());
        Assertions.assertNull(childrenDao.get(0L));
    }

    @Test
    void update(){
        List<Long> parents = new ArrayList<>();
        parents.add(4L);
        parents.add(10L);
        ChildrenEntity children = new ChildrenEntity(null,null,"2333",parents);
        ChildrenEntity childrenActual = childrenDao.get(2L);
        childrenDao.update(2L,1L,children);
        ChildrenEntity childrenUpdate = childrenDao.get(2L);
        Assertions.assertEquals(childrenActual.getName(),childrenUpdate.getName());
        Assertions.assertArrayEquals(childrenActual.getIdParents().toArray(),
                childrenUpdate.getIdParents().toArray());
        childrenDao.update(2L,2L,children);
        childrenUpdate = childrenDao.get(2L);
        Assertions.assertEquals(childrenUpdate.getName(),children.getName());
        Assertions.assertArrayEquals(childrenUpdate.getIdParents().toArray(),
                children.getIdParents().toArray());
    }

    @Test
    void delete(){
        try (Connection connection = dataSourceWrapper.getConnection();
            PreparedStatement ps = connection.prepareStatement(sccc)){
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<ChildrenEntity> childrenEntities = childrenDao.getAll();
        childrenDao.delete(5L,5L);
        List<ChildrenEntity> childrenEntitiesDelete = childrenDao.getAll();
        Assertions.assertEquals(childrenEntities.size()-1,childrenEntitiesDelete.size());
        Assertions.assertNull(childrenDao.get(5L));
    }

    @Test
    void exist(){
        Assertions.assertFalse(childrenDao.exist(0L));
        Assertions.assertTrue(childrenDao.exist(6L));
    }
}
