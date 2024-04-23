package dao.api;

import dao.entity.ParentEntity;

import java.util.List;

public interface IParentDao {
    void create(ParentEntity parentEntity);
    void update(long id, long version, ParentEntity parentEntity);
    void delete(long id, long version);
    ParentEntity get(long id);
    List<ParentEntity> getAll();
    boolean exist(long id);
}
