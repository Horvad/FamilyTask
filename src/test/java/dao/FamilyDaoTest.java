package dao;

import dao.api.IChildrenDao;
import dao.api.IFamilyDao;
import dao.ds.DataSourseC3P0;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.AddressEntity;
import dao.entity.ChildrenEntity;
import dao.entity.FamilyEntity;
import dao.entity.ParentEntity;
import dao.fabric.ChildrenDaoSingleton;
import dao.fabric.FamilyDaoSingleton;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;
import org.testcontainers.containers.PostgreSQLContainer;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FamilyDaoTest {
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
    static IFamilyDao familyDao;

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
        familyDao = FamilyDaoSingleton.getInstance(dataSourceWrapper);
    }

    @Test
    void addressGet (){
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement ps = connection.prepareStatement(sccc)){
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FamilyEntity familyEntity = familyDao.getFromAddress(1L);
        System.out.println(familyEntity);
        Assertions.assertEquals(familyEntity.getAddress().get(0).getStreet(),"Street1");
        Assertions.assertEquals(familyEntity.getAddress().get(0).getNumberHouse(),1);
        Assertions.assertEquals(familyEntity.getAddress().get(0).getNumberFlat(),11);
        List<ParentEntity> parentEntities = new ArrayList<>();
        try (Connection conn = dataSourceWrapper.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, name " +
                            "FROM app.parent " +
                            "WHERE id_address = 1;"
            )){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Long id = rs.getLong(1);
                String name = rs.getString(2);
                parentEntities.add(new ParentEntity(id,null,name,null));
            }
            parentEntities.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
            familyEntity.getParent().sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
            for(int i = 0; i<parentEntities.size();i++){
                Assertions.assertEquals(parentEntities.get(i).getName(),familyEntity.getParent().get(i).getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Set<ChildrenEntity> childrenEntities = new HashSet<>();
        for(ParentEntity parent : parentEntities){
            try (Connection conn = dataSourceWrapper.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT name " +
                            "FROM app.children " +
                            "   INNER JOIN app.children_parent_cross " +
                            "       ON app.children.id = app.children_parent_cross.id_children " +
                            "WHERE app.children_parent_cross.id_parent = ?;"
            )){
                ps.setLong(1,parent.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String name = rs.getString(1);
                    childrenEntities.add(new ChildrenEntity(null,null,name,null));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            familyEntity.getChildren().sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
            System.out.println(familyEntity.getChildren().toString());
            List<ChildrenEntity> childrenEntities1 = new ArrayList<>();
            for(ChildrenEntity children : childrenEntities1){
                childrenEntities1.add(children);
            }
            childrenEntities1.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            for(int i = 0;i<childrenEntities1.size();i++){
                Assertions.assertEquals(childrenEntities1.get(i).getName(),familyEntity.getChildren().get(i).getName());
            }
        }
    }

    @Test
    void getFromParent() {
        FamilyEntity familyEntity = familyDao.getFromParent(3L);
        AddressEntity address = new AddressEntity(null,null,null,0,0);
        try (Connection connection = dataSourceWrapper.getConnection();
        PreparedStatement ps = connection.prepareStatement("" +
                "SELECT street, number_flat, number_house " +
                "FROM app.address " +
                "INNER JOIN app.parent ON app.parent.id_address = app.address.id " +
                "WHERE app.parent.id = ?;")){
            ps.setLong(1,3L);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String street = rs.getString(1);
            Integer numberFlat = rs.getInt(2);
            Integer numberHouse = rs.getInt(3);
            Assertions.assertEquals(familyEntity.getAddress().get(0).getStreet(),street);
            Assertions.assertEquals(familyEntity.getAddress().get(0).getNumberFlat(),numberFlat);
            Assertions.assertEquals(familyEntity.getAddress().get(0).getNumberHouse(),numberHouse);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = dataSourceWrapper.getConnection();
        PreparedStatement ps = conn.prepareStatement("" +
                "SELECT app.children.name " +
                "FROM app.children " +
                "INNER JOIN app.children_parent_cross ON app.children.id = app.children_parent_cross.id_children " +
                "INNER JOIN app.parent ON app.parent.id = app.children_parent_cross.id_parent " +
                "WHERE app.parent.id = ?;")){
            ps.setLong(1,3L);
            Set<String> name = new HashSet<>();
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                name.add(resultSet.getString(1));
            }
            List<ChildrenEntity> childrenEntities = new ArrayList<>();
            for(String str : name){
                childrenEntities.add(new ChildrenEntity(null,null,str,null));
            }
            childrenEntities.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
            familyEntity.getChildren().sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
            for(int i = 0;i<childrenEntities.size();i++){
                Assertions.assertEquals(childrenEntities.get(i),familyEntity.getChildren().get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(familyEntity.toString());
    }

    @Test
    void children(){
        FamilyEntity familyEntities = familyDao.getFromChildren(1L);
        try (Connection conn = dataSourceWrapper.getConnection();
        PreparedStatement ps = conn.prepareStatement("" +
                "SELECT street, number_house, number_flat " +
                "FROM app.children " +
                "INNER JOIN app.children_parent_cross ON app.children.id = app.children_parent_cross.id_children " +
                "INNER JOIN app.parent ON app.parent.id = app.children_parent_cross.id_parent " +
                "INNER JOIN app.address ON app.address.id = app.parent.id_address " +
                "WHERE app.children.id = 1;")){
            ResultSet rs = ps.executeQuery();
            Set<AddressEntity> addressEntities = new HashSet<>();
            while (rs.next()){
                String street = rs.getString(1);
                Integer numberHouse = rs.getInt(2);
                Integer numberFlat = rs.getInt(3);
                addressEntities.add(new AddressEntity(null,null,street,numberFlat,numberHouse));
            }
            List<AddressEntity> addressList = new ArrayList<>();
            for(AddressEntity address : addressEntities){
                addressList.add(address);
            }
            addressList.sort(((o1, o2) -> o1.getStreet().compareTo(o2.getStreet())));
            familyEntities.getAddress().sort(((o1, o2) -> o1.getStreet().compareTo(o2.getStreet())));
            for(int i = 0;i<addressList.size();i++){
                Assertions.assertEquals(addressList.get(i).getStreet(),familyEntities.getAddress().get(i).getStreet());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = dataSourceWrapper.getConnection();
        PreparedStatement ps = conn.prepareStatement("" +
                "SELECT app.parent.name " +
                "FROM app.children " +
                "INNER JOIN app.children_parent_cross ON app.children.id = app.children_parent_cross.id_children " +
                "INNER JOIN app.parent ON app.parent.id = app.children_parent_cross.id_parent " +
                "WHERE app.children.id = 1;")){
            ResultSet rs = ps.executeQuery();
            Set<String> parents = new HashSet<>();
            while (rs.next()){
                String name = rs.getString(1);
                parents.add(name);
            }
            List<ParentEntity> parentEntities = new ArrayList<>();
            for(String parent : parents){
                parentEntities.add(new ParentEntity(null,null,parent,null));
            }
            familyEntities.getParent().sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            parentEntities.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            for(int i = 0; i<parentEntities.size();i++){
                Assertions.assertEquals(parentEntities.get(i).getName(),familyEntities.getParent().get(i).getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dataSourceWrapper.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
