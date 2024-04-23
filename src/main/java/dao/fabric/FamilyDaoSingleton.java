package dao.fabric;

import dao.FamilyDao;
import dao.api.IFamilyDao;
import dao.ds.api.IDataSourceWrapper;

public class FamilyDaoSingleton {
    private static volatile IFamilyDao instance;

    private FamilyDaoSingleton(){}

    public static IFamilyDao getInstance(IDataSourceWrapper dataSourceWrapper) {
        if(instance==null){
            synchronized (FamilyDaoSingleton.class){
                if(instance==null){
                    instance = new FamilyDao(dataSourceWrapper);
                }
            }
        }
        return instance;
    }
}
