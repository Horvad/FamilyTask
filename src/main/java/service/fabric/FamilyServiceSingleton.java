package service.fabric;

import dao.api.IFamilyDao;
import service.FamilyService;
import service.api.IAddressService;
import service.api.IChildrenService;
import service.api.IFamilyService;
import service.api.IParentService;

public class FamilyServiceSingleton {
    private static volatile IFamilyService instance;

    private FamilyServiceSingleton(){}

    public static IFamilyService getInstance(IFamilyDao familyDao,
                                             IAddressService addressService,
                                             IChildrenService childrenService,
                                             IParentService parentService) {
        if(instance==null){
            synchronized (FamilyServiceSingleton.class){
                if(instance==null){
                    instance = new FamilyService(
                            familyDao,
                            addressService,
                            childrenService,
                            parentService);
                }
            }
        }
        return instance;
    }
}
