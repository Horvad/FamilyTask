package dao.fabric;

import dao.AddressDao;
import dao.api.IAddressDao;
import dao.ds.api.IDataSourceWrapper;

public class AddressDaoSingleton {
    private static volatile IAddressDao instance;

    private AddressDaoSingleton(){};

    public static IAddressDao getInstance(IDataSourceWrapper dataSourceWrapper) {
        if(instance==null){
            synchronized (AddressDaoSingleton.class){
                if(instance==null){
                    instance = new AddressDao(dataSourceWrapper);
                }
            }
        }
        return instance;
    }
}
