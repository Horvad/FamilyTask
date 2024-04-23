package service.fabric;

import dao.api.IAddressDao;
import service.AddressService;
import service.api.IAddressService;

public class AddressServiceSingleton {
    private static volatile IAddressService instance;

    private AddressServiceSingleton() {
    }

    public static IAddressService getInstance(IAddressDao addressDao) {
        if(instance==null){
            synchronized (AddressServiceSingleton.class){
                if(instance==null){
                    instance = new AddressService(addressDao);
                }
            }
        }
        return instance;
    }
}
