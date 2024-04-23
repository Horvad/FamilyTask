package dao;

import dao.api.IChildrenDao;
import dao.ds.api.IDataSourceWrapper;
import dao.entity.ChildrenEntity;

import javax.print.attribute.standard.NumberUp;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class ChildrenDao implements IChildrenDao {
    private final IDataSourceWrapper ds;

    public ChildrenDao(IDataSourceWrapper dataSourceWrapper) {
        this.ds = dataSourceWrapper;
    }

    private final String CREATE_SQL = "" +
            "INSERT INTO app.children (name, version)" +
            "VALUES(?,?);";

    @Override
    public void create(ChildrenEntity childrenEntity) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);){
            ps.setString(1,childrenEntity.getName());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setLong(2,time);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            long id = rs.getLong(1);
            for(Long idPar : childrenEntity.getIdParents()){
                addParent(id,idPar);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private final String UPDATE_SQL = "" +
            "UPDATE app.children " +
            "SET name = ?, version = ? " +
            "WHERE id = ? AND version = ?;";
    @Override
    public void update(long id, long version, ChildrenEntity childrenEntity) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL);){
            ps.setString(1,childrenEntity.getName());
            long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setLong(2,time);
            ps.setLong(3, id);
            ps.setLong(4,version);
            int row = ps.executeUpdate();
            if(row!=0){
                deleteParent(id);
                for(long idParent : childrenEntity.getIdParents()){
                    addParent(id,idParent);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String DELETE_SQL = "" +
            "DELETE FROM app.children " +
            "WHERE id= ? AND version = ?; ";
    @Override
    public void delete(long id, long version) {
        ChildrenEntity children = get(id);
        if(children==null)
            return;
        if(children!=null||children.getVersion().equals(version)){
            deleteParent(id);
        }
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)){
            ps.setLong(1,id);
            ps.setLong(2,version);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_ID = "SELECT id, version, name, parents " +
            "FROM app.children FULL OUTER JOIN " +
            "(SELECT id_children, array_agg(id_parent) AS parents " +
            "FROM app.children_parent_cross " +
            "GROUP BY id_children) parent_arr " +
            "ON id = id_children " +
            "WHERE id = ?;";
    @Override
    public ChildrenEntity get(long id) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ID)){
            ps.setLong(1,id);
            ResultSet rs = ps.executeQuery();
            String name = null;
            Long version = null;

            if (rs.next()){
                version = rs.getLong(2);
                name = rs.getString(3);
            }
            if(name==null)
                return null;
            Array array = rs.getArray(4);
            Long[] parents = (Long[])array.getArray();
            Set<Long> set = new HashSet<>();
            for(Long parent : parents){
                set.add(parent);
            }
            return new ChildrenEntity(id,version,name, set.stream().toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_ALL = "" +
            "SELECT id, version, name, parents " +
            "FROM app.children FULL OUTER JOIN " +
            "(SELECT id_children, array_agg(id_parent) AS parents " +
            "FROM app.children_parent_cross " +
            "GROUP BY id_children) parent_arr " +
            "ON id = id_children; ";
    @Override
    public List<ChildrenEntity> getAll() {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL)){
            ResultSet rs = ps.executeQuery();
            List<ChildrenEntity> childrenEntities = new ArrayList<>();
            while (rs.next()){
                Long id = rs.getLong(1);
                Long version = rs.getLong(2);
                String name = rs.getString(3);
                Array array = rs.getArray(4);
                Long[] parents = (Long[])array.getArray();
                childrenEntities.add(new ChildrenEntity(id,version,name, Arrays.stream(parents).toList()));
            }
            return childrenEntities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exist(long id) {
        if(get(id)!=null)
            return true;
        return false;
    }

    private final String ADD_PARENT = "" +
            "INSERT INTO app.children_parent_cross (id_children, id_parent) " +
            "VALUES(?,?)";
    private void addParent(long idChildren, long idParent){
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(ADD_PARENT);){
            ps.setLong(1,idChildren);
            ps.setLong(2,idParent);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String DELETE_CHILDREN_SQL = "" +
            "DELETE FROM app.children_parent_cross " +
            "WHERE id_children = ? ";
    private void deleteParent(long idChildren){
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_CHILDREN_SQL)){
            ps.setLong(1,idChildren);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
