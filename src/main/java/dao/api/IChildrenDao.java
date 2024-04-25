package dao.api;

import dao.entity.ChildrenEntity;

import java.util.List;

/**
 *Интерфей предназначенный для сохраниния, получения, проверки на наличие, изменнения, удаления
 *  из хранилища сущностей типа "ребенок" {@see dao.entity.ChildrenEntity}
 */
public interface IChildrenDao {
    /**
     * Создание ребенка
     * @param childrenEntity
     */
    void create(ChildrenEntity childrenEntity);

    /**
     * Обновление ребенка
     * @param id
     * @param version
     * @param childrenEntity
     */
    void update(long id, long version, ChildrenEntity childrenEntity);

    /**
     * удаление
     * @param id
     * @param version
     */
    void delete(long id, long version);

    /**
     * получение ребенка по id
     * @param id
     * @return
     */
    ChildrenEntity get(long id);

    /**
     * Получение всех детей в хранилище
     * @return
     */
    List<ChildrenEntity> getAll();

    /**
     * Поверка на наличие в хранилище id
     * @param id
     * @return
     */
    boolean exist(long id);
}
