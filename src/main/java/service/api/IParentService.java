package service.api;

import core.createDTO.ParentCreateDTO;
import core.viewDTO.ParentViewDTO;

import java.util.List;
/**
 * интерфейс предназначен для получения родителей от источника данных преобразование,
 * проверку на валидность и сохранине в хранилище, а так же чтение из хранилища и отправку на
 * конечный пользователя (servlet, telegram и.т.д)
 */
public interface IParentService {
    /**
     * создание родителя
     * @param parentCreateDTO
     */
    void create(ParentCreateDTO parentCreateDTO);

    /**
     * обновление
     * @param id
     * @param version
     * @param parentCreateDTO
     */
    void update(long id, long version, ParentCreateDTO parentCreateDTO);

    /**
     * удаление
     * @param id
     * @param version
     */
    void delete(long id, long version);

    /**
     * получение по id
     * @param id
     * @return
     */
    ParentViewDTO get(long id);

    /**
     * получение всех
     * @return
     */
    List<ParentViewDTO> getAll();

    /**
     * поверка сущ. ли данный id в системе
     * @param id
     * @return
     */
    boolean exist(long id);
}
