package service.fabric;

import dao.api.IChildrenDao;
import service.ChildrenService;
import service.api.IChildrenService;
import service.api.IParentService;

public class ChildrenServiceSingleton {
    private static volatile IChildrenService instance;

    private ChildrenServiceSingleton() {
    }

    public static IChildrenService getInstance(IChildrenDao childrenDao, IParentService parentService) {
        if(instance==null){
            synchronized (ChildrenServiceSingleton.class){
                if(instance==null){
                    instance = new ChildrenService(childrenDao, parentService);
                }
            }
        }
        return instance;
    }
}
