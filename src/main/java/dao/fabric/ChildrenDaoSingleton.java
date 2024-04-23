package dao.fabric;

import dao.ChildrenDao;
import dao.api.IChildrenDao;
import dao.ds.api.IDataSourceWrapper;

public class ChildrenDaoSingleton {
    private static volatile IChildrenDao instance;

    private ChildrenDaoSingleton(){};

    public static IChildrenDao getInstance(IDataSourceWrapper dataSourceWrapper) {
        if(instance==null){
            synchronized (ChildrenDaoSingleton.class){
                if(instance==null){
                    instance = new ChildrenDao(dataSourceWrapper);
                }
            }
        }
        return instance;
    }
}
