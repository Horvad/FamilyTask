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

    /**
     * Хранилище данных и родительский сервис с которыми работает данный клас
     */
    public ChildrenService(IChildrenDao dao, IParentService parentService) {
        this.dao = dao;
        this.parentService = parentService;
    }

    /**
     * Создание {@link dao.entity.ChildrenEntity} из {@param childrenCreateDTO} при валидности
     * addressCreateDTO
     * @param childrenCreateDTO
     * @exception IllegalArgumentException
     */
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

    /**
     * обновление объекта {@link dao.entity.ChildrenEntity} ключами {@param childrenCreateDTO}
     * при валидности версии, id, addressCreateDTO
     * @param id
     * @param version - актуальная версия переменной
     * @param childrenCreateDTO
     * @exception IllegalArgumentException
     */
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

    /**
     * удаление ребенка с данным id при валидности версии
     * @param id
     * @param version - - текущая версия объекта
     * @exception IllegalArgumentException
     */
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

    /**
     * Получение {@link core.viewDTO.ChildrenViewDTO} по id
     * @param id
     * @return
     * @exception IllegalArgumentException
     */
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

    /**
     * получение всех детей из хранилища
     * @return
     */
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

    /**
     * проверка на присутсвие в хранилище id
     * @param id
     * @return
     */
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
