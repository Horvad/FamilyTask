package service;

import core.FamilyAddressDTO;
import core.FamilyDTOChildren;
import core.FamilyDTOParent;
import dao.FamilyDao;
import dao.api.IFamilyDao;
import dao.entity.AddressEntity;
import dao.entity.ChildrenEntity;
import dao.entity.FamilyEntity;
import dao.entity.ParentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import service.api.IAddressService;
import service.api.IChildrenService;
import service.api.IFamilyService;
import service.api.IParentService;
import service.fabric.FamilyServiceSingleton;

import java.util.ArrayList;
import java.util.List;

public class FamilyServiceTest {
    static IFamilyService familyService;
    @Mock
    static IFamilyDao familyDao;
    @Mock
    static IChildrenService childrenService;
    @Mock
    static IAddressService addressService;
    @Mock
    static IParentService parentService;

    @BeforeAll
    public static void init(){
        familyDao = Mockito.mock(FamilyDao.class);
        childrenService = Mockito.mock(ChildrenService.class);
        addressService = Mockito.mock(AddressService.class);
        parentService = Mockito.mock(ParentService.class);
        familyService = FamilyServiceSingleton.getInstance(
            familyDao,addressService,childrenService,parentService
        );
    }

    @Test
    public void getFromParent(){
        Mockito.when(parentService.exist(1L)).thenReturn(true);
        ArrayList<ParentEntity> parentEntities = new ArrayList<>();
        ArrayList<AddressEntity> addressEntities = new ArrayList<>();
        ArrayList<ChildrenEntity> childrenEntities = new ArrayList<>();
        parentEntities.add(new ParentEntity(null,null,"One",null));
        addressEntities.add(new AddressEntity(null,null,"StreetOne",11,1));
        childrenEntities.add(new ChildrenEntity(null,null, "childOne",null));
        childrenEntities.add(new ChildrenEntity(null,null,"childTwo",null));
        Mockito.when(familyDao.getFromParent(1L))
                .thenReturn(new FamilyEntity(
                        addressEntities,
                        childrenEntities,
                        parentEntities
                ));
        Mockito.when(parentService.exist(2L)).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class,()->familyService.getFromParent(2L));
        FamilyDTOParent parent = familyService.getFromParent(1L);
        List<String> child = new ArrayList<>();
        child.add("childOne");
        child.add("childTwo");
        Assertions.assertEquals(parent.getName(),"One");
        parent.getChildrens().sort((o1, o2) -> o1.compareTo(o2));
        for(int i = 0;i<child.size();i++){
            Assertions.assertEquals(child.get(i),(parent.getChildrens().get(i)));
        }
        Assertions.assertEquals(parent.getNumberFlat(),11);
        Assertions.assertEquals(parent.getStreet(),"StreetOne");
        Assertions.assertEquals(parent.getNumberHouse(),1);
    }

    @Test
    public void childrenService(){
        Mockito.when(childrenService.exist(1L)).thenReturn(true);
        Mockito.when(childrenService.exist(2L)).thenReturn(false);
        List<AddressEntity> address = new ArrayList<>();
        List<ChildrenEntity> childrens = new ArrayList<>();
        List<ParentEntity> parents = new ArrayList<>();
        childrens.add(new ChildrenEntity(1L,null,"OneChildren",null));
        address.add(new AddressEntity(10L,null,"Streetten",101,100));
        parents.add(new ParentEntity(21L,null,"Axex",10L));
        parents.add(new ParentEntity(22L,null, "Mia", 10L));
        Assertions.assertThrows(IllegalArgumentException.class, ()->familyService.getFromChildren(2L));
        Mockito.when(familyDao.getFromChildren(1L)).thenReturn(new FamilyEntity(
                address,
                childrens,
                parents
        ));
        FamilyDTOChildren childrenViewDTO = familyService.getFromChildren(1L);
        parents.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        childrenViewDTO.getParrents().sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        Assertions.assertEquals(childrenViewDTO.getName(),"OneChildren");
        for(int i = 0;i<parents.size();i++){
                Assertions.assertEquals(parents.get(i).getName(),childrenViewDTO.getParrents().get(i).getName());
                Assertions.assertEquals(childrenViewDTO.getParrents().get(i).getStreet(),"Streetten");
                Assertions.assertEquals(childrenViewDTO.getParrents().get(i).getNumberFlat(),101);
                Assertions.assertEquals(childrenViewDTO.getParrents().get(i).getNumberHouse(),100);
            }
        Assertions.assertThrows(IllegalArgumentException.class,()->familyService.getFromChildren(2L));
 };


    @Test
    public void address(){
        Mockito.when(addressService.exist(1L)).thenReturn(true);
        Mockito.when(addressService.exist(2L)).thenReturn(false);
        List<AddressEntity> addressEntities = new ArrayList<>();
        List<ParentEntity> parentEntities = new ArrayList<>();
        List<ChildrenEntity> childrenEntities = new ArrayList<>();
        addressEntities.add(new AddressEntity(null,null,"StreenTen",221,2));
        parentEntities.add(new ParentEntity(null,null, "alex", null));
        childrenEntities.add(new ChildrenEntity(null,null, "max",null));
        childrenEntities.add(new ChildrenEntity(null,null,"CJ",null));
        Mockito.when(familyDao.getFromAddress(1L)).thenReturn(new FamilyEntity(
                addressEntities,
                childrenEntities,
                parentEntities
        ));
        Assertions.assertThrows(IllegalArgumentException.class,()->familyService.getFromAddress(2L));
        FamilyAddressDTO address = familyService.getFromAddress(1L);
        Assertions.assertEquals(address.getStreet(),"StreenTen");
        Assertions.assertEquals(address.getNumberFlat(),221);
        Assertions.assertEquals(address.getNumberHouse(),2);
        parentEntities.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        childrenEntities.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        address.getChildrens().sort((o1, o2) -> o1.compareTo(o2));
        address.getParents().sort((o1, o2) -> o1.compareTo(o2));
        for(int i = 0; i<address.getParents().size();i++){
            Assertions.assertEquals(parentEntities.get(i).getName(),address.getParents().get(i));
        }
    }
}
