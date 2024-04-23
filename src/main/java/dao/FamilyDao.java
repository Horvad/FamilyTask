package dao;

import dao.api.IFamilyDao;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.AddressEntity;
import dao.entity.ChildrenEntity;
import dao.entity.FamilyEntity;
import dao.entity.ParentEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FamilyDao implements IFamilyDao {
    private final IDataSourceWrapper ds;

    public FamilyDao(IDataSourceWrapper dataSourceWrapper) {
        this.ds = dataSourceWrapper;
    }

    private final String GET_PARENT_SQL = "" +
            "SELECT app.parent.name, " +
            "       childrens, " +
            "       app.address.street, " +
            "       app.address.number_house, " +
            "       app.address.number_flat " +
            "FROM app.parent FULL OUTER JOIN " +
            "      (SELECT array_agg(app.children.name) AS childrens, id_parent" +
            "       FROM  app.children_parent_cross FULL OUTER JOIN app.children ON id_children = app.children.id " +
            "       GROUP BY id_parent " +
            "       HAVING id_parent = ?) children_arr " +
            "   ON id = id_parent " +
            "   FULL OUTER JOIN app.address ON app.parent.id_address = app.address.id " +
            "WHERE app.parent.id = ?;";
    @Override
    public FamilyEntity getFromParent(long id) {
        try (Connection con = ds.getConnection()){
            PreparedStatement ps = con.prepareStatement(GET_PARENT_SQL);
            ps.setLong(1,id);
            ps.setLong(2,id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String parentName = rs.getString(1);
            String[] childrens = (String[]) rs.getArray(2).getArray();
            Set<String> childr = new HashSet<>();
            for(String child : childrens){
                childr.add(child);
            }
            String street = rs.getString(3);
            Integer numberHouse = rs.getInt(4);
            Integer numberFlat = rs.getInt(5);
            List<ParentEntity> parentEntities = new ArrayList<>();
            parentEntities.add(new ParentEntity(null,null,parentName,null));
            List<ChildrenEntity> childrenEntities = new ArrayList<>();
            for(String children : childr){
                childrenEntities.add(new ChildrenEntity(null,null, children,null));
            }
            List<AddressEntity> addressEntities = new ArrayList<>();
            addressEntities.add(new AddressEntity(null,null,street,numberFlat, numberHouse));
            return new FamilyEntity(addressEntities,childrenEntities,parentEntities);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_CHILDREN_SQL = "" +
            "SELECT " +
            "   app.children.id, " +
            "   app.children.name, " +
            "   app.parent.id AS parent_id, " +
            "   app.parent.name AS parent_name, " +
            "   app.parent.id_address, " +
            "   app.address.id AS address_id, " +
            "   app.address.street, " +
            "   app.address.number_house, " +
            "   app.address.number_flat " +
            "FROM app.children" +
            "   INNER JOIN app.children_parent_cross ON app.children.id = app.children_parent_cross.id_children " +
            "   INNER JOIN app.parent ON app.parent.id = app.children_parent_cross.id_parent " +
            "   INNER JOIN app.address ON app.parent.id_address = app.address.id " +
            "WHERE app.children.id = ?;";
    @Override
    public FamilyEntity getFromChildren(long id) {
        try (Connection con = ds.getConnection()){
            PreparedStatement ps = con.prepareStatement(GET_CHILDREN_SQL);
            ps.setLong(1,id);
            Set<ChildrenEntity> childrens = new HashSet<>();
            Set<ParentEntity> parents = new HashSet<>();
            Set<AddressEntity> addreses = new HashSet<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Long idChild = rs.getLong(1);
                String nameChild = rs.getString(2);
                Long idParent = rs.getLong(3);
                String nameParent = rs.getString(4);
                Long idAddressParent = rs.getLong(5);
                Long idAddress = rs.getLong(6);
                String street = rs.getString(7);
                Integer numberHouse = rs.getInt(8);
                Integer numberFlat = rs.getInt(9);
                childrens.add(new ChildrenEntity(idChild,null,nameChild,null));
                parents.add(new ParentEntity(idParent,null,nameParent,idAddressParent));
                addreses.add(new AddressEntity(idAddress,null,street,numberFlat,numberHouse));
            }
            List<ParentEntity> parentEntities = new ArrayList<>();
            for(ParentEntity parent : parents){
                parentEntities.add(parent);
            }
            List<ChildrenEntity> childrenEntities = new ArrayList<>();
            for(ChildrenEntity children : childrens){
                childrenEntities.add(children);
            }
            List<AddressEntity> addressEntities = new ArrayList<>();
            for(AddressEntity address : addreses){
                addressEntities.add(address);
            }
            return new FamilyEntity(addressEntities,childrenEntities,parentEntities);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private final String ADDRESS_SQL = "" +
            "SELECT " +
            "   array_agg(app.children.name) AS children_name, " +
            "   array_agg(app.parent.name) AS parent_name, " +
            "   app.address.street, " +
            "   app.address.number_house," +
            "   app.address.number_flat " +
            "FROM app.children " +
            "   INNER JOIN app.children_parent_cross ON app.children.id = app.children_parent_cross.id_children " +
            "   INNER JOIN app.parent ON app.parent.id = app.children_parent_cross.id_parent " +
            "   INNER JOIN app.address ON app.parent.id_address = app.address.id " +
            "GROUP BY app.address.id " +
            "HAVING app.address.id = ?;";
    @Override
    public FamilyEntity getFromAddress(long id) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(ADDRESS_SQL);){
            ps.setLong(1,id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Array arrayChildren = rs.getArray(1);
            String[] childrenStr = (String[]) arrayChildren.getArray();
            Set<String> childrens = new HashSet<>();
//                    Set.of((String[]) rs.getArray(1).getArray());
            for(String childrensS : childrenStr){
                childrens.add(childrensS);
            }
            Array arrayPar = rs.getArray(2);
            String[] arrParSrt = (String[])arrayPar.getArray();
            Set<String> parents = new HashSet<>();
//                    Set.of((String) rs.getArray(2).getArray());
            for(String strPar : arrParSrt){
                parents.add(strPar);
            }
            String street = rs.getString(3);
            Integer numberHouse = rs.getInt(4);
            Integer numberFlat = rs.getInt(5);
            List<AddressEntity> addressEntities = new ArrayList<>();
            List<ChildrenEntity> childrenEntities = new ArrayList<>();
            List<ParentEntity> parentEntities = new ArrayList<>();
            for(String children : childrens){
                childrenEntities.add(new ChildrenEntity(null,null,children,null));
            }
            for(String parent : parents){
                parentEntities.add(new ParentEntity(null,null,parent,null));
            }
            addressEntities.add(new AddressEntity(null,null,street,numberFlat, numberHouse));
            return new FamilyEntity(
                    addressEntities,
                    childrenEntities,
                    parentEntities
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

