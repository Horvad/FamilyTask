package dao;

import dao.api.IParentDao;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.ParentEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса IParentDao{@see dao.api.IParentDao} с использованием PostgresSQL
 */

public class ParentDao implements IParentDao {
    private final IDataSourceWrapper ds;

    public ParentDao(IDataSourceWrapper dataSourceWrapper) {
        this.ds = dataSourceWrapper;
    }

    private String CREATE_SQL = "INSERT INTO app.parent (name, id_address, version) VALUES(?,?,?);";
    private String UPDATE_SQL = "" +
            "UPDATE app.parent " +
            "SET name = ?, id_address = ?, version = ? " +
            "WHERE id = ? AND version = ?";

    private final String DELETE_SQL =
            "DELETE FROM app.parent WHERE id = ? AND version = ?";

    private final String GET_ID_SQL =
            "SELECT id, version, name, id_address " +
                    "FROM app.parent " +
                    "WHERE id = ?";

    private final String GET_ALL_SQL =
            "SELECT id, version, name, id_address " +
                    "FROM app.parent";

    /**
     * содание сущности
     * @param parentEntity
     */
    @Override
    public void create(ParentEntity parentEntity) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);){
            ps.setString(1,parentEntity.getName());
            ps.setLong(2,parentEntity.getAddress());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setLong(3,time);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * обновление сущности
     * @param id
     * @param version - актуальная версия объекта, если версии переданные в метод
     *                  и находящиется в хранилище, то дейсвия проводить нельзя
     * @param parentEntity
     */
    @Override
    public void update(long id, long version, ParentEntity parentEntity) {
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL);){
            preparedStatement.setString(1,parentEntity.getName());
            preparedStatement.setLong(2,parentEntity.getAddress());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            preparedStatement.setLong(3,time);
            preparedStatement.setLong(4,id);
            preparedStatement.setLong(5,version);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * удаление сущности
     * @param id
     * @param version - актуальная версия объекта, если версии переданные в метод
     *                и находящиется в хранилище, то дейсвия проводить нельзя
     */
    @Override
    public void delete(long id, long version) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL);){
            ps.setLong(1,id);
            ps.setLong(2,version);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * получение сущности по id
     * @param id
     * @return
     */
    @Override
    public ParentEntity get(long id) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ID_SQL);){
            ps.setLong(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                long idEnt = rs.getLong(1);
                long version = rs.getLong(2);
                String name = rs.getString(3);
                long idAddress = rs.getLong(4);
                if(name==null){
                    return null;
                }
                return new ParentEntity(idEnt,version,name,idAddress);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * получение всех сущностей
     * @return
     */
    @Override
    public List<ParentEntity> getAll() {
        List<ParentEntity> parentEntities = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement();){
            ResultSet rs = statement.executeQuery(GET_ALL_SQL);
            while (rs.next()){
                long idEnt = rs.getLong(1);
                long version = rs.getLong(2);
                String name = rs.getString(3);
                long idAddress = rs.getLong(4);
                if(name!=null){
                    parentEntities.add(new ParentEntity(idEnt,version,name,idAddress));
                }
                if(parentEntities.isEmpty()){
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parentEntities;
    }

    /**
     * проверка еслть ли id в БД
     * @param id
     * @return
     */
    @Override
    public boolean exist(long id) {
        if(get(id)==null){
            return false;
        } else{
            return true;
        }
    }
}
