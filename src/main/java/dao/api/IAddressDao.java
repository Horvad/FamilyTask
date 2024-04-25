package dao.api;

import dao.entity.AddressEntity;

import java.util.List;

/**
 *Интерфей предназначенный для сохраниния, получения, проверки на наличие, изменнения, удаления
 *  из хранилища сущностей типа "адресс" {@see dao.entity.AddressEntity}
 */
public interface IAddressDao {
    /**
     * созание адреса
     * @param addressEntity
     */
    void create(AddressEntity addressEntity);

    /**
     * Изменения адреса
     * @param id
     * @param version
     * @param addressEntity
     */
    void update(long id, long version, AddressEntity addressEntity);

    /**
     * Удаления адреса
     * @param id
     * @param version
     */
    void delete(long id, long version);

    /**
     * Получение адреса по id
     * @param id
     * @return
     */
    AddressEntity get(long id);

    /**
     * Полечение всех адресов
     * @return
     */
    List<AddressEntity> getAll();

    /**
     * Проверка на наличие id в хранилище
     * @param id
     * @return
     */
    boolean exist(long id);
}
