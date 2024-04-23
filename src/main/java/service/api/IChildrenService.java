package service.api;

import core.createDTO.ChildrenCreateDTO;
import core.viewDTO.ChildrenViewDTO;

import java.util.List;

public interface IChildrenService {
    void create(ChildrenCreateDTO childrenCreateDTO);
    void update(long id, long version, ChildrenCreateDTO childrenCreateDTO);
    void delete(long id, long version);
    ChildrenViewDTO get(long id);
    List<ChildrenViewDTO> getAll();
    boolean exist(long id);
}
