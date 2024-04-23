package service;

import core.createDTO.ChildrenCreateDTO;
import core.viewDTO.ChildrenViewDTO;
import dao.api.IChildrenDao;
import dao.entity.ChildrenEntity;
import service.api.IChildrenService;
import service.api.IParentService;

import java.util.ArrayList;
import java.util.List;

public class ChildrenService implements IChildrenService {
    private final IChildrenDao dao;
    private final IParentService parentService;

    public ChildrenService(IChildrenDao dao, IParentService parentService) {
        this.dao = dao;
        this.parentService = parentService;
    }

    @Override
    public void create(ChildrenCreateDTO childrenCreateDTO) {
        validate(childrenCreateDTO);
        dao.create(new ChildrenEntity(
                null,
                null,
                childrenCreateDTO.getName(),
                childrenCreateDTO.getIdParent()
        ));
    }

    @Override
    public void update(long id, long version, ChildrenCreateDTO childrenCreateDTO) {
        validate(childrenCreateDTO);
        ChildrenEntity children = dao.get(id);
        if(children==null){
            throw new IllegalArgumentException("Данного id не существует");
        }
        if(children.getVersion()!=version)
            throw new IllegalArgumentException("Ваша версия устарела");
        validate(childrenCreateDTO);
        children.setName(childrenCreateDTO.getName());
        children.setIdParents(childrenCreateDTO.getIdParent());
        dao.update(id,version,children);
    }

    @Override
    public void delete(long id, long version) {
        ChildrenEntity children = dao.get(id);
        if(children==null){
            throw new IllegalArgumentException("Данного id не существует");
        }
        if(children.getVersion()==version){
            dao.delete(id,version);
        } else {
            throw new IllegalArgumentException("Ваша версия устарела");
        }
    }

    @Override
    public ChildrenViewDTO get(long id) {
        ChildrenEntity children = dao.get(id);
        if(children==null)
            throw new IllegalArgumentException("Данного id не существует");
        return new ChildrenViewDTO(
                children.getId(),
                children.getVersion(),
                children.getName(),
                children.getIdParents()
        );
    }

    @Override
    public List<ChildrenViewDTO> getAll() {
        List<ChildrenEntity> childrenEntities = dao.getAll();
        List<ChildrenViewDTO> childrenViewDTOS = new ArrayList<>();
        for(ChildrenEntity children : childrenEntities){
            childrenViewDTOS.add(new ChildrenViewDTO(
                    children.getId(),
                    children.getVersion(),
                    children.getName(),
                    children.getIdParents()
            ));
        }
        return childrenViewDTOS;
    }

    @Override
    public boolean exist(long id) {
        return dao.exist(id);
    }

    private void validate(ChildrenCreateDTO children){
        if(children.getName()==null||children.getName().isEmpty()||children.getName().isBlank())
            throw new IllegalArgumentException("Не верно введно имя");
        if(children.getIdParent()==null||children.getIdParent().isEmpty())
            throw new IllegalArgumentException("Не верно введен родитель");
        for(Long id : children.getIdParent()){
            if(!parentService.exist(id))
                throw new IllegalArgumentException("Родителя с id "+id+" не существует");
        }
    }
}
