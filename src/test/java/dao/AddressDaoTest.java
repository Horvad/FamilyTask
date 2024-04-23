package dao;


import dao.api.IAddressDao;
import dao.ds.DataSourseC3P0;
import dao.ds.api.IDataSourceWrapper;
import dao.ds.fabric.DataSourceSingleton;
import dao.entity.AddressEntity;
import dao.fabric.AddressDaoSingleton;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddressDaoTest {
        static String dbName = "postgres";
        static String login = "postgres";
        static String pass = "123";
        static String schema = "" +
                "CREATE SCHEMA IF NOT EXISTS app" +
                "    AUTHORIZATION postgres;" +
                "CREATE TABLE IF NOT EXISTS app.address " +
                "( " +
                "       id bigserial NOT NULL, " +
                "       version bigint NOT NULL, " +
                "       street text NOT NULL, " +
                "       number_house integer NOT NULL, " +
                "       number_flat integer NOT NULL, " +
                "       CONSTRAINT address_pkey PRIMARY KEY (id)" +
                ");";
        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
                .withDatabaseName(dbName)
                .withUsername(login)
                .withPassword(pass);
        static IAddressDao addressDao;

        @BeforeAll
        static void before(){
                postgreSQLContainer.start();
        }

        @AfterAll
        static void after(){
                postgreSQLContainer.stop();
        }

        @BeforeEach
        void setUp() throws PropertyVetoException {
                Properties properties = new Properties();
                properties.put("db.class",postgreSQLContainer.getDriverClassName());
                properties.put("db.url",postgreSQLContainer.getJdbcUrl());
                properties.put("db.login",postgreSQLContainer.getUsername());
                properties.put("db.password",postgreSQLContainer.getPassword());
                DataSourceSingleton.setProperties(properties);
                IDataSourceWrapper dataSourceWrapper = DataSourceSingleton.getInstance();
                addressDao = AddressDaoSingleton.getInstance(dataSourceWrapper);
                try (Connection conn = dataSourceWrapper.getConnection();
                        PreparedStatement preparedStatement = conn.prepareStatement(schema);){
                        preparedStatement.executeUpdate();
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }
        }



        @Test
        void createAndGetAll(){
                List<AddressEntity> addressEntities = addressDao.getAll();
                addressDao.create(new AddressEntity(null,null,"1",1,1));
                addressDao.create(new AddressEntity(null,null,"2",2,2));
                List<AddressEntity> addressEntitiesNew = addressDao.getAll();
                Assertions.assertEquals(addressEntities.size()+2,addressEntitiesNew.size());
                addressEntitiesNew.sort(((o1, o2) -> o1.getId().compareTo(o2.getId())));
                AddressEntity address = addressEntitiesNew.get(addressEntitiesNew.size()-2);
                AddressEntity addressTwo = addressEntitiesNew.get(addressEntitiesNew.size()-1);
                Assertions.assertNotNull(address.getId());
                Assertions.assertNotNull(address.getVersion());
                Assertions.assertEquals(address.getStreet(),"1");
                Assertions.assertEquals(address.getNumberHouse(),1);
                Assertions.assertEquals(address.getNumberFlat(),1);
                Assertions.assertNotNull(addressTwo.getId());
                Assertions.assertNotNull(addressTwo.getVersion());
                Assertions.assertEquals(addressTwo.getStreet(),"2");
                Assertions.assertEquals(addressTwo.getNumberHouse(),2);
                Assertions.assertEquals(addressTwo.getNumberFlat(),2);
                Assertions.assertEquals(address.getId()+1,addressTwo.getId());
                Assertions.assertNotEquals(address.getVersion(),addressTwo.getVersion());
        }

        @Test
        void getId(){
                addressDao.create(new AddressEntity(null,null,"4",4,4));
                List<AddressEntity> addressEntities = addressDao.getAll();
                AddressEntity addressEntityActual = addressEntities.get(addressEntities.size()-1);
                AddressEntity addressEntity = addressDao.get(addressEntityActual.getId());
                Assertions.assertEquals(addressEntity,addressEntityActual);
                AddressEntity address = addressDao.get(0L);
                Assertions.assertEquals(address,null);
        }

        @Test
        void update(){
                addressDao.create(new AddressEntity(null,null,"5",5,5));
                List<AddressEntity> addressEntities = addressDao.getAll();
                AddressEntity addressEntityActual = new AddressEntity(addressEntities.get(0).getId()
                        , 0L
                        , addressEntities.get(0).getStreet()
                        , addressEntities.get(0).getNumberFlat()
                        , addressEntities.get(0).getNumberHouse());
                addressEntityActual.setStreet("5000000");
                addressEntityActual.setNumberFlat(5000000);
                addressEntityActual.setNumberHouse(5000001);
                addressDao.update(addressEntityActual.getId(),addressEntityActual.getVersion(),addressEntityActual);
                Assertions.assertEquals(addressEntities.get(0),addressDao.get(addressEntityActual.getId()));

                addressDao.update(addressEntities.get(0).getId(),
                        addressEntities.get(0).getVersion(),
                        addressEntityActual);
                AddressEntity addressEntity = addressDao.get(addressEntityActual.getId());
                Assertions.assertEquals(addressEntityActual.getStreet(),addressEntity.getStreet());
                Assertions.assertEquals(addressEntityActual.getNumberFlat(),addressEntity.getNumberFlat());
                Assertions.assertEquals(addressEntityActual.getNumberHouse(),addressEntity.getNumberHouse());
        }

        @Test
        void delete(){
                addressDao.create(new AddressEntity(null,null,"6",6,6));
                List<AddressEntity> addressEntities = addressDao.getAll();
                addressDao.delete(0L,addressEntities.get(0).getVersion());
                List<AddressEntity> addressEntitiesDelete = addressDao.getAll();
                Assertions.assertEquals(addressEntitiesDelete.size(),addressEntities.size());
                addressDao.delete(addressEntities.get(0).getId(),0L);
                addressEntitiesDelete = addressDao.getAll();
                Assertions.assertEquals(addressEntitiesDelete.size(),addressEntities.size());
                addressDao.delete(addressEntities.get(0).getId(),addressEntities.get(0).getVersion());
                addressEntitiesDelete = addressDao.getAll();
                Assertions.assertEquals(addressEntities.size(),addressEntitiesDelete.size()+1);
        }

        @Test
        void exist(){
                Assertions.assertFalse(addressDao.exist(0L));
                addressDao.create(new AddressEntity(null,null,"7",7,7));
                List<AddressEntity> addressEntities = addressDao.getAll();
                Assertions.assertTrue(addressDao.exist(addressEntities.get(addressEntities.size()-1).getId()));
        }
}
