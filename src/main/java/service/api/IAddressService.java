package service.api;

import core.createDTO.AddressCreateDTO;
import core.viewDTO.AddressViewDTO;

import java.util.List;

public interface IAddressService {
    void create(AddressCreateDTO addressCreateDTO);
    void update(long id, long versionId, AddressCreateDTO addressCreateDTO);
    void delete(long id, long versionId);
    AddressViewDTO get(long id);
    List<AddressViewDTO> getAll();
    boolean exist(long id);
}
