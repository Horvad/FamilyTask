package service;

import core.createDTO.ParentCreateDTO;
import core.viewDTO.ParentViewDTO;
import dao.ParentDao;
import dao.api.IParentDao;
import dao.entity.ParentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import service.api.IAddressService;
import service.api.IParentService;
import service.fabric.ParentServiceSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ParentServiceTest {
    private static IParentService parentService;

    @Mock
    private static IParentDao mockDao;
    @Mock
    private static IAddressService mockAddressService;

    @BeforeAll
    public static void init(){
        mockDao = Mockito.mock(ParentDao.class);
        mockAddressService = Mockito.mock(AddressService.class);
        parentService = ParentServiceSingleton.getInstance(mockDao,mockAddressService);
    }

    @Test
    public void create(){
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        parentService.create(new ParentCreateDTO("1",1L));
        ParentEntity parentEntity = new ParentEntity(null,null,"1",1L);
        Mockito.verify(mockDao).create(Mockito.eq(parentEntity));
    }

    @Test
    public void createValidation(){
        Mockito.when(mockAddressService.exist(2L)).thenReturn(false);
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.create(new ParentCreateDTO("1",2L)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.create(new ParentCreateDTO(null,1L)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.create(new ParentCreateDTO("",1L)));
    }

    @Test
    public void update(){
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        Mockito.when(mockAddressService.exist(3L)).thenReturn(true);
        Mockito.when(mockDao.get(1L)).thenReturn(new ParentEntity(1l,1L,"1",1L));
        parentService.update(1L,1L,new ParentCreateDTO("3",3L));
        Mockito.verify(mockDao).update(
                Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(new ParentEntity(1L,1L,"3",3L)));
    }

    @Test
    public void updateValidation(){
        Mockito.when(mockAddressService.exist(2L)).thenReturn(false);
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        Mockito.when(mockDao.get(2L)).thenReturn(null);
        Mockito.when(mockDao.get(3L)).thenReturn(new ParentEntity(3L,3L,"3",3L));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.update(3L,3L,new ParentCreateDTO(null,1L)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.update(3L,3L,new ParentCreateDTO("",1L)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.update(3L,3L,new ParentCreateDTO("2",2L)));
    }

    @Test
    public void updateNotFoundId(){
        Mockito.when(mockAddressService.exist(2L)).thenReturn(false);
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        Mockito.when(mockDao.get(2L)).thenReturn(null);
        Mockito.when(mockDao.get(3L)).thenReturn(new ParentEntity(3L,3L,"3",3L));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.update(2L,2L,new ParentCreateDTO("2",1L)));
    }

    @Test
    public void updateNotValidVersion(){
        Mockito.when(mockAddressService.exist(2L)).thenReturn(false);
        Mockito.when(mockAddressService.exist(1L)).thenReturn(true);
        Mockito.when(mockDao.get(2L)).thenReturn(null);
        Mockito.when(mockDao.get(3L)).thenReturn(new ParentEntity(3L,3L,"3",3L));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.update(3L,2L,new ParentCreateDTO("2",1L)));
    }

    @Test
    public void delete(){
        Mockito.when(mockDao.get(3L)).thenReturn(new ParentEntity(3L,3L,"3",3L));
        parentService.delete(3L,3L);
        Mockito.verify(mockDao).delete(Mockito.eq(3L),Mockito.eq(3L));
    }

    @Test
    public void deleteException(){
        Mockito.when(mockDao.get(4L)).thenReturn(null);
        Mockito.when(mockDao.get(5L)).thenReturn(new ParentEntity(5L,5L,"5",5L));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.delete(4L,4L));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.delete(5L,4L));
    }

    @Test
    public void get(){
        Mockito.when(mockDao.get(6L)).thenReturn(new ParentEntity(6L,6L,"6",6L));
        ParentViewDTO parentViewDTO = new ParentViewDTO(6L,6L,"6",6L);
        Assertions.assertEquals(parentViewDTO,parentService.get(6L));
    }

    @Test
    public void getNotFoundId(){
        Mockito.when(mockDao.get(7L)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->parentService.get(7L));
    }

    @Test
    public void getAll(){
        List<ParentEntity> parentEntities = new ArrayList<>();
        List<ParentViewDTO> parentViewDTOS = new ArrayList<>();
        for(long i = 1;i<11;i++){
            parentEntities.add(new ParentEntity(i,i,String.valueOf(i),i));
            parentViewDTOS.add(new ParentViewDTO(i,i,String.valueOf(i),i));
        }
        Mockito.when(mockDao.getAll()).thenReturn(parentEntities);
        Collections.sort(parentViewDTOS,((o1, o2) -> Long.compare(o1.getId(),o2.getId())));
        List<ParentViewDTO> getAllPar = parentService.getAll();
        Collections.sort(getAllPar,((o1, o2) -> Long.compare(o1.getId(),o2.getId())));
        Assertions.assertEquals(getAllPar,parentViewDTOS);
    }

    @Test
    public void exist(){
        Mockito.when(mockDao.exist(7L)).thenReturn(true);
        Mockito.when(mockDao.exist(8L)).thenReturn(false);
        Assertions.assertEquals(parentService.exist(7L),true);
        Assertions.assertEquals(parentService.exist(8L),false);
    }
}
