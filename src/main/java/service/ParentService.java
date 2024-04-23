package service;

import core.createDTO.ParentCreateDTO;
import core.viewDTO.ParentViewDTO;
import dao.api.IParentDao;
import dao.entity.ParentEntity;
import service.api.IAddressService;
import service.api.IParentService;

import java.util.ArrayList;
import java.util.List;

public class ParentService implements IParentService {
    private final IParentDao dao;
    private final IAddressService addressService;

    public ParentService(IParentDao dao, IAddressService addressService) {
        this.dao = dao;
        this.addressService = addressService;
    }

    @Override
    public void create(ParentCreateDTO parentCreateDTO) {
        validation(parentCreateDTO);
        dao.create(new ParentEntity(
                null,
                null,
                parentCreateDTO.getName(),
                parentCreateDTO.getIdAddress()));
    }

    @Override
    public void update(long id, long version, ParentCreateDTO parentCreateDTO) {
        validation(parentCreateDTO);
        ParentEntity parentEntity = dao.get(id);
        if(parentEntity==null)
            throw new IllegalArgumentException("Родителя с данным id не существует");
        if(!parentEntity.getVersion().equals(version))
            throw new IllegalArgumentException("Ваш объект устарел, обновите объект");
        parentEntity.setAddress(parentCreateDTO.getIdAddress());
        parentEntity.setName(parentCreateDTO.getName());
        dao.update(id,version,parentEntity);
    }

    @Override
    public void delete(long id, long version) {
        ParentEntity parentEntity = dao.get(id);
        if(parentEntity==null){
            throw new IllegalArgumentException("Родителя с данным id не сешествует");
        }
        if(!parentEntity.getVersion().equals(version))
            throw new IllegalArgumentException("Ваш объект устарел, обновите объект");
        dao.delete(id,version);
    }

    @Override
    public ParentViewDTO get(long id) {
        ParentEntity parentEntity = dao.get(id);
        if(parentEntity==null){
            throw new IllegalArgumentException("Родителя с данным id не сешествует");
        }
        return new ParentViewDTO(
                parentEntity.getId(),
                parentEntity.getVersion(),
                parentEntity.getName(),
                parentEntity.getAddress()
        );
    }

    @Override
    public List<ParentViewDTO> getAll() {
        List<ParentViewDTO> parentViewDTOS = new ArrayList<>();
        List<ParentEntity> parentEntities = dao.getAll();
        for(ParentEntity parentEntity : parentEntities){
            ParentViewDTO parentViewDTO = new ParentViewDTO(
                    parentEntity.getId(),
                    parentEntity.getVersion(),
                    parentEntity.getName(),
                    parentEntity.getAddress()
            );
            parentViewDTOS.add(parentViewDTO);
        }
        return parentViewDTOS;
    }

    @Override
    public boolean exist(long id) {
        return dao.exist(id);
    }

    private void validation(ParentCreateDTO parentCreateDTO){
        if(parentCreateDTO.getName()==null||parentCreateDTO.getName().isBlank()||parentCreateDTO.getName().isEmpty())
            throw new IllegalArgumentException("Не верно введены данные");
        if(!addressService.exist(parentCreateDTO.getIdAddress())){
            throw new IllegalArgumentException("Не верно введен id адреса");
        }
    }
}
