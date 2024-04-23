package service.fabric;

import dao.api.IParentDao;
import service.ParentService;
import service.api.IAddressService;
import service.api.IParentService;

public class ParentServiceSingleton {
    private static volatile IParentService instance;

    private ParentServiceSingleton(){};

    public static IParentService getInstance(IParentDao parentDao, IAddressService addressService) {
        if(instance==null){
            synchronized (ParentServiceSingleton.class){
                if(instance==null){
                    instance = new ParentService(parentDao, addressService);
                }
            }
        }
        return instance;
    }
}
