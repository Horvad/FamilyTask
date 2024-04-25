package dao;

import dao.api.IAddressDao;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.AddressEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
/**
 * Реализация интерфейса IAddressDao{@see dao.api.IAddressDao} с использованием PostgresSQL
 */
public class AddressDao implements IAddressDao {
    private final IDataSourceWrapper ds;
    private final String CREATE_SQL =
            "INSERT INTO app.address (street, number_house, number_flat, version) " +
                    "VALUES(?,?,?,?)";

    private final String UPDATE_SQL =
            "UPDATE app.address SET " +
                    "street = ?, " +
                    "number_house = ?, " +
                    "number_flat = ?, " +
                    "version = ? " +
                    "WHERE id = ? AND version = ?;";

    private final String DELETE_SQL =
            "DELETE FROM app.address WHERE id = ? AND version = ?";

    private final String GET_ID_SQL =
            "SELECT id, version, street, number_house, number_flat " +
                    "FROM app.address " +
                    "WHERE id = ?;";

    private final String GET_ALL_SQL =
            "SELECT id, version, street, number_house, number_flat FROM app.address;";

    /**
     *
     * @param dataSourceWrapper - текущий dataSource
     */
    public AddressDao(IDataSourceWrapper dataSourceWrapper) {
        this.ds = dataSourceWrapper;
    }

    /**
     * Создание сущности адрес
     * @param addressEntity
     */
    @Override
    public void create(AddressEntity addressEntity) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_SQL)){
            ps.setString(1, addressEntity.getStreet());
            ps.setInt(2,addressEntity.getNumberHouse());
            ps.setInt(3,addressEntity.getNumberFlat());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setLong(4,time);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * обновление сущности адрес
     * @param id
     * @param version - актуальная версия сущности, если весия переданная и хранящаяяся в базе не совпадают,
     *                то обновления не произойдет
     * @param addressEntity
     */
    @Override
    public void update(long id, long version, AddressEntity addressEntity) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
            ps.setString(1,addressEntity.getStreet());
            ps.setInt(2,addressEntity.getNumberHouse());
            ps.setInt(3,addressEntity.getNumberFlat());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setLong(4,time);
            ps.setLong(5,id);
            ps.setLong(6,version);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * удаление сущности
     * @param id
     * @param version - актуальная версия сущности, если весия переданная и
     *                хранящаяяся в базе не совпадают, то удаления не произойдет
     */
    @Override
    public void delete(long id, long version) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
            ps.setLong(1,id);
            ps.setLong(2,version);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * полечение сущности по id
     * @param id
     * @return
     */
    @Override
    public AddressEntity get(long id) {
        AddressEntity addressEntity = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ID_SQL);){
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                long version = resultSet.getLong("version");
                String street = resultSet.getString("street");
                int numberHouse = resultSet.getInt("number_house");
                int numberFlat = resultSet.getInt("number_flat");
                if(street==null){
                    return null;
                }
                addressEntity = new AddressEntity(id,version,street,numberFlat,numberHouse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addressEntity;
    }

    /**
     * полечение всез сущностей
     * @return
     */
    @Override
    public List<AddressEntity> getAll() {
        List<AddressEntity> addressEntities = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(GET_ALL_SQL);
            while (resultSet.next()){
                long id = resultSet.getLong(1);
                long version = resultSet.getLong(2);
                String street = resultSet.getString(3);
                int numberHouse = resultSet.getInt(4);
                int numberFlat = resultSet.getInt(5);
                addressEntities.add(new AddressEntity(
                        id,
                        version,
                        street,
                        numberFlat,
                        numberHouse
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addressEntities;
    }

    /**
     * проверка есть ли в БД данный id
     * @param id
     * @return
     */
    @Override
    public boolean exist(long id) {
        AddressEntity addressEntity = get(id);
        if(addressEntity!=null)
            return true;
        return false;
    }
}
