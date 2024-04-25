package service.api;

import core.createDTO.AddressCreateDTO;
import core.viewDTO.AddressViewDTO;

import java.util.List;

/**
 * интерфейс предназначен для получения адресов от источника данных преобразование,
 * проверку на валидность и сохранине в хранилище, а так же чтение из хранилища и отправку на
 * конечный пользователя (servlet, telegram и.т.д)
 */
public interface IAddressService {
    /**
     * Создание переменной адрес
     * @param addressCreateDTO
     */
    void create(AddressCreateDTO addressCreateDTO);

    /**
     * Обновление переменной адрес
     * @param id
     * @param versionId - актуальная версия переменной
     * @param addressCreateDTO
     */
    void update(long id, long versionId, AddressCreateDTO addressCreateDTO);

    /**
     * удаление адреса
     * @param id
     * @param versionId - актуальная версия переменной
     */
    void delete(long id, long versionId);

    /**
     * Получение адреса по id
     * @param id
     * @return
     */
    AddressViewDTO get(long id);

    /**
     * Получение всех адресов
     * @return
     */
    List<AddressViewDTO> getAll();

    /**
     * Проверка наличия адреса с хранилище
     * @param id
     * @return
     */
    boolean exist(long id);
}
