package dao.api;

import dao.entity.AddressEntity;

import java.util.List;

public interface IAddressDao {
    void create(AddressEntity addressEntity);
    void update(long id, long version, AddressEntity addressEntity);
    void delete(long id, long version);
    AddressEntity get(long id);
    List<AddressEntity> getAll();
    boolean exist(long id);
}
