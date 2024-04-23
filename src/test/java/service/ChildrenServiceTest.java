package service;

import core.createDTO.ChildrenCreateDTO;
import core.viewDTO.ChildrenViewDTO;
import dao.ChildrenDao;
import dao.api.IChildrenDao;
import dao.entity.ChildrenEntity;
import dao.entity.ParentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import service.api.IChildrenService;
import service.api.IParentService;
import service.fabric.ChildrenServiceSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChildrenServiceTest {
    private static IChildrenService childrenService;

    @Mock
    private static IParentService mockParentService;
    @Mock
    private static IChildrenDao mockDao;

    @BeforeAll
    public static void init(){
        mockDao = Mockito.mock(ChildrenDao.class);
        mockParentService = Mockito.mock(ParentService.class);
        childrenService = ChildrenServiceSingleton.getInstance(mockDao,mockParentService);
    }

    @Test
    public void create(){
        Mockito.when(mockParentService.exist(1L)).thenReturn(true);
        Mockito.when(mockParentService.exist(2L)).thenReturn(true);
        List<Long> parents = new ArrayList<>();
        parents.add(1L);
        parents.add(2L);
        childrenService.create(new ChildrenCreateDTO("1",parents));
        ChildrenEntity children = new ChildrenEntity(null,null,"1",parents);
        Mockito.verify(mockDao).create(Mockito.eq(children));
    }

    @Test
    public void createValidate(){
        Mockito.when(mockParentService.exist(3L)).thenReturn(true);
        Mockito.when(mockParentService.exist(4L)).thenReturn(true);
        List<Long> parent = new ArrayList<>();
        parent.add(3L);
        parent.add(4L);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO(null,parent)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO("",parent)));
    }

    @Test
    public void createValidteParent(){
        Mockito.when(mockParentService.exist(5L)).thenReturn(true);
        Mockito.when(mockParentService.exist(6L)).thenReturn(false);
        List<Long> parent = new ArrayList<>();
        parent.add(5L);
        parent.add(6L);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO("4",parent)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO("5",null)));
        parent.clear();
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO("5",parent)));
    }

    @Test
    public void update(){
        Mockito.when(mockParentService.exist(7L)).thenReturn(true);
        Mockito.when(mockParentService.exist(8L)).thenReturn(true);
        List<Long> parent = new ArrayList<>();
        parent.add(7L);
        parent.add(8L);
        Mockito.when(mockDao.get(1L)).thenReturn(new ChildrenEntity(1L,1L,"1",parent));
        parent.remove(7L);
        childrenService.update(1L,1L,new ChildrenCreateDTO("2",parent));
        Mockito.verify(mockDao).update(Mockito.eq(1L),
                Mockito.eq(1L),
                Mockito.eq(new ChildrenEntity(1L,1L,"2",parent)));
    }

    @Test
    public void updateValidate(){
        Mockito.when(mockParentService.exist(9L)).thenReturn(true);
        Mockito.when(mockParentService.exist(10L)).thenReturn(true);
        Mockito.when(mockParentService.exist(11L)).thenReturn(false);

        List<Long> parent = new ArrayList<>();
        parent.add(9L);
        parent.add(10L);
        Mockito.when(mockDao.get(1L)).thenReturn(new ChildrenEntity(1L,1L,"1",parent));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(1L,1L,new ChildrenCreateDTO(null,parent)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(1L,1L, new ChildrenCreateDTO("",parent)));
        parent.add(11L);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(1L,1L, new ChildrenCreateDTO("4",parent)));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(1L,1L, new ChildrenCreateDTO("5",null)));
        parent.clear();
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.create(new ChildrenCreateDTO("5",parent)));
    }

    @Test
    public void updateNotFoundId(){
        Mockito.when(mockParentService.exist(12L)).thenReturn(true);
        Mockito.when(mockParentService.exist(13L)).thenReturn(true);

        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(3L)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(3L,1L,new ChildrenCreateDTO("2",parent)));
    }

    @Test
    public void updateNotCorrectVersion(){
        Mockito.when(mockParentService.exist(12L)).thenReturn(true);
        Mockito.when(mockParentService.exist(13L)).thenReturn(true);

        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(3L)).thenReturn(new ChildrenEntity(3L,3L,"1",parent));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->childrenService.update(3L,2L,new ChildrenCreateDTO("3",parent)));
    }

    @Test
    public void delete(){
        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(4L)).thenReturn(new ChildrenEntity(4L,4L,"1",parent));
        childrenService.delete(4L,4L);
        Mockito.verify(mockDao).delete(Mockito.eq(4L),Mockito.eq(4L));
    }

    @Test
    public void deleteNotFountId(){
        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(5L)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,()->childrenService.delete(5L,5L));
    }

    @Test
    public void deleteNotCorrectVersion(){
        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(6L)).thenReturn(new ChildrenEntity(6L,6L,"1",parent));
        Assertions.assertThrows(IllegalArgumentException.class,()->childrenService.delete(6L,5L));}

    @Test
    public void get(){
        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(7L)).thenReturn(new ChildrenEntity(7L, 7L, "7",parent));
        ChildrenViewDTO childrenViewDTO = new ChildrenViewDTO(7L, 7L, "7",parent);
        ChildrenViewDTO childrenViewGet = childrenService.get(7L);
        Assertions.assertEquals(childrenViewDTO,childrenViewGet);
    }

    @Test
    public void getNotFoundId(){
        List<Long> parent = new ArrayList<>();
        parent.add(12L);
        parent.add(13L);
        Mockito.when(mockDao.get(8L)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, ()->childrenService.get(8L));
    }

    @Test
    public void getAll(){
        List<Long> parentOne = new ArrayList<>();
        parentOne.add(14L);
        parentOne.add(15L);
        List<Long> parentTwo = new ArrayList<>();
        parentTwo.add(15L);
        parentTwo.add(16L);
        List<Long> parentTree = new ArrayList<>();
        parentTree.add(14L);
        parentTree.add(16L);
        List<ChildrenEntity> childrenEntities = new ArrayList<>();
        childrenEntities.add(new ChildrenEntity(8L,8L,"8",parentOne));
        childrenEntities.add(new ChildrenEntity(9L,9L,"9",parentTwo));
        childrenEntities.add(new ChildrenEntity(10L,10L,"10",parentTree));
        Mockito.when(mockDao.getAll()).thenReturn(childrenEntities);
        List<ChildrenViewDTO> childrenViewDTOS = childrenService.getAll();
        List<ChildrenViewDTO> equalList = new ArrayList<>();
        equalList.add(new ChildrenViewDTO(8L,8L,"8",parentOne));
        equalList.add(new ChildrenViewDTO(9L,9L,"9",parentTwo));
        equalList.add(new ChildrenViewDTO(10L,10L,"10",parentTree));
        Collections.sort(childrenViewDTOS,((o1, o2) -> Long.compare(o1.getId(), o2.getId())));
        Collections.sort(childrenViewDTOS,((o1, o2) -> Long.compare(o1.getId(),o2.getId())));
        Assertions.assertEquals(childrenViewDTOS,equalList);
    }

    @Test
    public void exist(){
        Mockito.when(mockDao.exist(11L)).thenReturn(true);
        Mockito.when(mockDao.exist(12L)).thenReturn(false);
        Assertions.assertEquals(childrenService.exist(11L),true);
        Assertions.assertEquals(childrenService.exist(12L),false);
    }
}
