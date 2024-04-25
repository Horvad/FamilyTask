package service.api;

import core.createDTO.ChildrenCreateDTO;
import core.viewDTO.ChildrenViewDTO;

import java.util.List;

/**
 * интерфейс предназначен для получения детей от отсочника данных преобразование,
 * проверку на валидность и сохранине в хранилище, а так же чтение из хранилища и отправку на
 * конечный пользователя (servlet, telegram и.т.д)
 */
public interface IChildrenService {
    /**
     * создание ребенка
     * @param childrenCreateDTO
     */
    void create(ChildrenCreateDTO childrenCreateDTO);

    /**
     * обновление данных
     * @param id
     * @param version - текущая версия объекта
     * @param childrenCreateDTO
     */
    void update(long id, long version, ChildrenCreateDTO childrenCreateDTO);

    /**
     * удаление данных
     * @param id
     * @param version - - текущая версия объекта
     */
    void delete(long id, long version);

    /**
     * получение данных по id
     * @param id
     * @return
     */
    ChildrenViewDTO get(long id);

    /**
     * получение всех детей
     * @return
     */
    List<ChildrenViewDTO> getAll();

    /**
     * проверка существут ли id в системе
     * @param id
     * @return
     */
    boolean exist(long id);
}
