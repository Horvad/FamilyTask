package dao.api;

import dao.entity.ChildrenEntity;

import java.util.List;

public interface IChildrenDao {
    void create(ChildrenEntity childrenEntity);
    void update(long id, long version, ChildrenEntity childrenEntity);
    void delete(long id, long version);
    ChildrenEntity get(long id);
    List<ChildrenEntity> getAll();
    boolean exist(long id);
}
