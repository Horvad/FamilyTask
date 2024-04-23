package service.api;

import core.createDTO.ParentCreateDTO;
import core.viewDTO.ParentViewDTO;

import java.util.List;

public interface IParentService {
    void create(ParentCreateDTO parentCreateDTO);
    void update(long id, long version, ParentCreateDTO parentCreateDTO);
    void delete(long id, long version);
    ParentViewDTO get(long id);
    List<ParentViewDTO> getAll();
    boolean exist(long id);
}
