package dao.api;

import dao.entity.ParentEntity;

import java.util.List;
/**
 *Интерфей предназначенный для сохраниния, получения, проверки на наличие, изменнения, удаления
 *  из хранилища сущностей типа "родитель" {@see dao.entity.ParentEntity}
 */
public interface IParentDao {
    /**
     * Создание родителя
     * @param parentEntity
     */
    void create(ParentEntity parentEntity);

    /**
     * Обновление родителя
     * @param id
     * @param version - актуальная версия объекта, если версии переданные в метод
     *                  и находящиется в хранилище, то дейсвия проводить нельзя
     * @param parentEntity
     */
    void update(long id, long version, ParentEntity parentEntity);

    /**
     * Удаление родителя
     * @param id
     * @param version - актуальная версия объекта, если версии переданные в метод
     *                и находящиется в хранилище, то дейсвия проводить нельзя
     */
    void delete(long id, long version);

    /**
     * Получение по id
     * @param id
     * @return
     */
    ParentEntity get(long id);

    /**
     * Получение всех объектов
     * @return
     */
    List<ParentEntity> getAll();

    /**
     * проверка наличия id в хранилище
     * @param id
     * @return
     */
    boolean exist(long id);
}
