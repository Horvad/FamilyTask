package dao.fabric;

import dao.ParentDao;
import dao.api.IParentDao;
import dao.ds.api.IDataSourceWrapper;

public class ParentDaoSingleton {
    private static volatile IParentDao instance;

    private ParentDaoSingleton(){}

    public static IParentDao getInstance(IDataSourceWrapper dataSourceWrapper) {
        if(instance==null){
            synchronized (ParentDaoSingleton.class){
                if(instance==null){
                    instance = new ParentDao(dataSourceWrapper);
                }
            }
        }
        return instance;
    }
}
