package service;

import core.FamilyAddressDTO;
import core.FamilyDTOChildren;
import core.FamilyDTOParent;
import core.ParentFromFamilyDTO;
import dao.api.IFamilyDao;
import dao.entity.AddressEntity;
import dao.entity.ChildrenEntity;
import dao.entity.FamilyEntity;
import dao.entity.ParentEntity;
import service.api.IAddressService;
import service.api.IChildrenService;
import service.api.IFamilyService;
import service.api.IParentService;

import java.util.ArrayList;
import java.util.List;

public class FamilyService implements IFamilyService {
    private final IFamilyDao dao;
    private final IAddressService addressService;
    private final IChildrenService childrenService;
    private final IParentService parentService;

    public FamilyService(IFamilyDao familyDao,
                         IAddressService addressService,
                         IChildrenService childrenService,
                         IParentService parentService) {
        this.dao = familyDao;
        this.addressService = addressService;
        this.childrenService = childrenService;
        this.parentService = parentService;
    }

    @Override
    public FamilyDTOParent getFromParent(long id) {
        if(!parentService.exist(id))
            throw new IllegalArgumentException("Данного id не существует");
        FamilyEntity entity = dao.getFromParent(id);
        String name = entity.getParent().get(0).getName();
        List<String> childrens = new ArrayList<>();
        for(ChildrenEntity children : entity.getChildren()){
            childrens.add(children.getName());
        }
        String street = entity.getAddress().get(0).getStreet();
        long numberHouse = entity.getAddress().get(0).getNumberHouse();
        long numberFlat = entity.getAddress().get(0).getNumberFlat();
        FamilyDTOParent familyDTOParent = new FamilyDTOParent();
        familyDTOParent.setName(name);
        familyDTOParent.setChildrens(childrens);
        familyDTOParent.setStreet(street);
        familyDTOParent.setNumberHouse(numberHouse);
        familyDTOParent.setNumberFlat(numberFlat);
        return familyDTOParent;
    }

    @Override
    public FamilyDTOChildren getFromChildren(long id) {
            if(!childrenService.exist(id))
            throw new IllegalArgumentException("Данного id не существует");
        FamilyEntity entity = dao.getFromChildren(id);
        String name = entity.getChildren().get(0).getName();
        List<ParentFromFamilyDTO> listParent = new ArrayList<>();
        for(ParentEntity parentEntity : entity.getParent()){
            ParentFromFamilyDTO parent = new ParentFromFamilyDTO();
            parent.setName(parentEntity.getName());
            for(AddressEntity addressEntity : entity.getAddress()){
                if(addressEntity.getId().equals(parentEntity.getAddress())){
                    parent.setStreet(addressEntity.getStreet());
                    parent.setNumberHouse(addressEntity.getNumberHouse());
                    parent.setNumberFlat(addressEntity.getNumberFlat());
                    break;
                }
            }
            listParent.add(parent);
        }
        return new FamilyDTOChildren(name,listParent);
    }

    @Override
    public FamilyAddressDTO getFromAddress(long id) {
        if(!addressService.exist(id))
            throw new IllegalArgumentException("Данного id не существует");
        FamilyEntity entity = dao.getFromAddress(id);
        String street = entity.getAddress().get(0).getStreet();
        long numberHouse = entity.getAddress().get(0).getNumberHouse();
        long numberFlat = entity.getAddress().get(0).getNumberFlat();
        List<String> parent = new ArrayList<>();
        for(ParentEntity parentEntity : entity.getParent()){
            parent.add(parentEntity.getName());
        }
        List<String> children = new ArrayList<>();
        for(ChildrenEntity childrenEntity : entity.getChildren()){
            children.add(childrenEntity.getName());
        }
        return new FamilyAddressDTO(
                street,
                numberHouse,
                numberFlat,
                parent,
                children
        );
    }
}
