package service;

import core.createDTO.AddressCreateDTO;
import core.viewDTO.AddressViewDTO;
import dao.api.IAddressDao;
import dao.entity.AddressEntity;
import service.api.IAddressService;

import java.util.ArrayList;
import java.util.List;

public class AddressService implements IAddressService {
    /**
     * Хранилище данных с которой работает данный класс
     */
    private final IAddressDao dao;

    public AddressService(IAddressDao dao) {
        this.dao = dao;
    }

    /**
     * Создание {@link dao.entity.AddressEntity} из {@param addressCreateDTO} при валидности
     * addressCreateDTO
     * @param addressCreateDTO
     * @exception IllegalArgumentException
     */
    @Override
    public void create(AddressCreateDTO addressCreateDTO) {
        validate(addressCreateDTO);
        AddressEntity addressEntity = new AddressEntity(
                null,
                null,
                addressCreateDTO.getStreet(),
                addressCreateDTO.getNumberHouse(),
                addressCreateDTO.getNumberFlat());
        dao.create(addressEntity);
    }

    /**
     * обновление объекта {@link dao.entity.AddressEntity} ключами {@param addressCreateDTO}
     * при валидности версии, id, addressCreateDTO
     * @param id
     * @param versionId - актуальная версия переменной
     * @param addressCreateDTO
     * @exception IllegalArgumentException
     */
    @Override
    public void update(long id, long versionId, AddressCreateDTO addressCreateDTO) {
        validate(addressCreateDTO);
        AddressEntity addressEntity = dao.get(id);
        if(addressEntity == null)
            throw new IllegalArgumentException("Данного id не существует");
        if(addressEntity.getVersion() != versionId)
            throw new IllegalArgumentException("Версия объекта не софпадает, обновите объект");
        addressEntity.setStreet(addressCreateDTO.getStreet());
        addressEntity.setNumberFlat(addressCreateDTO.getNumberFlat());
        addressEntity.setNumberHouse(addressCreateDTO.getNumberHouse());
        dao.update(id,versionId,addressEntity);
    }

    /**
     * удаление из бд объекта с данним id при валидности версии
     * @param id
     * @param versionId - актуальная версия переменной
     * @exception IllegalArgumentException
     */
    @Override
    public void delete(long id, long versionId) {
        AddressEntity addressEntity = dao.get(id);
        if(addressEntity == null)
            throw new IllegalArgumentException("Данного id не существует");
        if(!addressEntity.getVersion().equals(id))
            throw new IllegalArgumentException("Версия объекта не софпадает, обновите объект");
        dao.delete(id,versionId);
    }

    /**
     * получение {@link core.viewDTO.AddressViewDTO} их БД по id
     * @param id
     * @return
     * @exception IllegalArgumentException
     */
    @Override
    public AddressViewDTO get(long id) {
        AddressEntity addressEntity = dao.get(id);
        if(addressEntity==null)
            throw new IllegalArgumentException("Данного id не существет");
        return new AddressViewDTO(
                addressEntity.getId(),
                addressEntity.getVersion(),
                addressEntity.getStreet(),
                addressEntity.getNumberHouse(),
                addressEntity.getNumberFlat()
        );
    }

    /**
     * получение всех адресов из БД
     * @return
     */
    @Override
    public List<AddressViewDTO> getAll() {
        List<AddressEntity> addressEntities = dao.getAll();
        List<AddressViewDTO> addressViewDTOS = new ArrayList<>();
        for(AddressEntity addressEntity : addressEntities){
            addressViewDTOS.add(new AddressViewDTO(
                    addressEntity.getId(),
                    addressEntity.getVersion(),
                    addressEntity.getStreet(),
                    addressEntity.getNumberHouse(),
                    addressEntity.getNumberFlat()
            ));
        }
        return addressViewDTOS;
    }

    /**
     * Проверка на наличие id
     * @param id
     * @return
     */
    @Override
    public boolean exist(long id) {
        return dao.exist(id);
    }

    private void validate(AddressCreateDTO addressCreateDTO){
        if(addressCreateDTO.getStreet()==null||addressCreateDTO.getStreet().isBlank())
            throw new IllegalArgumentException("Не введен адресс");
        if(addressCreateDTO.getNumberHouse()<1)
            throw new IllegalArgumentException("Не корректно введен номер дома");
        if(addressCreateDTO.getNumberFlat()<1)
            throw new IllegalArgumentException("Не корректно введен номер квартиры");
    }
}
