package service;

import core.createDTO.AddressCreateDTO;
import core.viewDTO.AddressViewDTO;
import dao.AddressDao;
import dao.api.IAddressDao;
import dao.entity.AddressEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import service.api.IAddressService;
import service.fabric.AddressServiceSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AddressServiceTest {
    static  IAddressService addressService;
    @Mock
    static IAddressDao mockAddressDao;

    @BeforeAll
    public static void setUp(){
        mockAddressDao = Mockito.mock(AddressDao.class);
        addressService = AddressServiceSingleton.getInstance(mockAddressDao);
    }
    @Test
    public void addAddress(){
        addressService.create(new AddressCreateDTO("1",1,1));
        AddressEntity addressEntity = new AddressEntity(null,null,"1",1,1);
        Mockito.verify(mockAddressDao).create(Mockito.eq(addressEntity));
    }

    @Test void addAddressException(){
        Assertions.assertThrows(IllegalArgumentException.class,()->addressService.create(new AddressCreateDTO(null,1,1)));
        Assertions.assertThrows(IllegalArgumentException.class,()->addressService.create(new AddressCreateDTO("1",-1,1)));
        Assertions.assertThrows(IllegalArgumentException.class,()->addressService.create(new AddressCreateDTO("1",1,-1)));
    }

    @Test
    public void updateAddress(){
        Mockito.when(mockAddressDao.get(5)).thenReturn(new AddressEntity(5L,5L, "1",1,1));
        addressService.update(5L,5L,new AddressCreateDTO("2",2,2));
        AddressEntity addressEntity = new AddressEntity(5L,5L,"2",2,2);
        Mockito.verify(mockAddressDao).update(Mockito.eq(5L),Mockito.eq(5L),Mockito.eq(addressEntity));
    }

    @Test
    public void updateNotFoundId(){
        Mockito.when(mockAddressDao.get(6)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->addressService.update(6L,6L,new AddressCreateDTO("2",2,2)));
    }

    @Test
    public void updateNotCorrectVersion(){
        Mockito.when(mockAddressDao.get(7)).thenReturn(new AddressEntity(7L,7L, "1",1,1));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->addressService.update(7L,8L,new AddressCreateDTO("2",2,2)));
    }

    @Test
    public void validateUpdate(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(new AddressEntity(1L,1L, "1",1,1));
        addressService.update(1L,1L,new AddressCreateDTO("2",2,2));
        AddressEntity addressEntity = new AddressEntity(1L,1L,"2",2,2);
        Mockito.verify(mockAddressDao).update(Mockito.eq(1L),Mockito.eq(1L),Mockito.eq(addressEntity));
    }

    @Test
    public void delete(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(new AddressEntity(1L,1L, "1",1,1));
        addressService.delete(1L,1L);
        Mockito.verify(mockAddressDao).delete(Mockito.eq(1L),Mockito.eq(1L));
    }

    @Test
    public void deleteNotFoundId(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->addressService.delete(1L,1L));
    }

    @Test
    public void deleteNotCorrectVersion(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(new AddressEntity(1L,2L, "1",1,1));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->addressService.delete(1L,1L));
    }

    @Test
    public void get(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(new AddressEntity(1L,2L, "1",1,1));
        AddressViewDTO addressViewDTO = new AddressViewDTO(1,2,"1",1,1);
        Assertions.assertEquals(addressViewDTO, addressService.get(1L));
    }

    @Test
    public void getNotFountId(){
        Mockito.when(mockAddressDao.get(1)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->addressService.get(1));
    }

    @Test
    public void getAll(){
        List<AddressEntity> addressEntities = new ArrayList<>();
        List<AddressViewDTO> addressViewDTO = new ArrayList<>();
        for(int i = 0; i<10;i++){
            addressEntities.add(new AddressEntity(Long.valueOf(i+1),Long.valueOf(i+1),String.valueOf(i),i,i));
            addressViewDTO.add(new AddressViewDTO(Long.valueOf(i+1),Long.valueOf(i+1),String.valueOf(i),i,i));
        }
        Mockito.when(mockAddressDao.getAll()).thenReturn(addressEntities);
        List<AddressViewDTO> newList = addressService.getAll();
        Collections.sort(addressViewDTO,((o1, o2) -> Long.compare(o1.getId(),o2.getId())));
        Collections.sort(newList,((o1, o2) -> Long.compare(o1.getId(),o2.getId())));
        Assertions.assertEquals(newList,addressViewDTO);
    }

    @Test
    public void exist(){
        Mockito.when(mockAddressDao.exist(1L)).thenReturn(true);
        Mockito.when(mockAddressDao.exist(2L)).thenReturn(false);
        Assertions.assertEquals(addressService.exist(1L),true);
        Assertions.assertEquals(addressService.exist(2L),false);
    }
}
